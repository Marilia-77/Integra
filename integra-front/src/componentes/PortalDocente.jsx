import { useState, useEffect } from "react";
import "./PortalDocente.css";
import NavbarDocente from "./NavbarDocente";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

export default function PortalDocente() {
    const navigate = useNavigate();
    const [professor, setProfessor] = useState(null);
    const [alunos, setAlunos] = useState([]);
    const [ocorrencias, setOcorrencias] = useState([]);
    const [carregando, setCarregando] = useState(true);
    const [erroAlunos, setErroAlunos] = useState("");

    const [novaOcorrencia, setNovaOcorrencia] = useState({
        titulo: "", 
        data: new Date().toISOString().split('T')[0], 
        descricao: "", 
        categoria: "Pedagógica", 
        idAluno: ""
    });
    const [mensagemOcorrencia, setMensagemOcorrencia] = useState("");

    const [recomendacao, setRecomendacao] = useState("");
    const [carregandoIA, setCarregandoIA] = useState(false);

    useEffect(() => {
        carregarDados();
    }, []);

    async function carregarDados() {
        try {
            const resProf = await api.get("/professores/me");
            setProfessor(resProf.data);

            try {
                const resAlunos = await api.get("/aluno");
                setAlunos(resAlunos.data);
            } catch (err) {
                setErroAlunos(`Erro ao carregar alunos (${err.response?.status}): ${err.response?.data?.mensagem || err.message}`);
            }

            try {
                const resOcorrencias = await api.get(`/ocorrencia/professor/${resProf.data.idProfessor}`);
                setOcorrencias(resOcorrencias.data);
            } catch (err) {
                console.warn("Erro nas ocorrências:", err);
            }

        } catch (error) {
            if (error.response?.status === 401 || error.response?.status === 403) {
                localStorage.clear();
                navigate("/login");
            }
        } finally {
            setCarregando(false);
        }
    }

    async function handleCriarOcorrencia(e) {
        e.preventDefault();
        setMensagemOcorrencia("");
        try {
            await api.post("/ocorrencia", {
                ...novaOcorrencia,
                idProfessor: professor.idProfessor,
                idAluno: Number(novaOcorrencia.idAluno)
            });
            setMensagemOcorrencia("Ocorrência registrada com sucesso!");
            setNovaOcorrencia({ 
                titulo: "", 
                data: new Date().toISOString().split('T')[0], 
                descricao: "", 
                categoria: "Pedagógica", 
                idAluno: "" 
            });
            carregarDados();
        } catch (error) {
            setMensagemOcorrencia("Erro ao registrar ocorrência.");
        }
    }

    async function gerarRecomendacao(idOcorrencia) {
        setRecomendacao("");
        setCarregandoIA(true);
        try {
            const response = await api.post(`/recomendacoes/gerar/${idOcorrencia}`);
            setRecomendacao(response.data.texto);
        } catch (error) {
            setRecomendacao("Erro ao gerar recomendação com a IA.");
        } finally {
            setCarregandoIA(false);
        }
    }

    function sair() {
        localStorage.clear();
        navigate("/login");
    }

    if (carregando) return <p style={{ padding: "20px" }}>Carregando painel...</p>;

    return (
        <div className="portal-docente">
            <NavbarDocente ativa="dashboard" nomeProfessor={professor?.nomeProfessor || "Professor"} />

            <main className="docente-conteudo">
                <div style={{ display: "flex", justifyContent: "space-between", marginTop: "20px" }}>
                    <h2>Bem-vindo, {professor?.nomeProfessor}</h2>
                    <button onClick={sair} style={{ padding: "8px 16px", cursor: "pointer", background: "#d32f2f", color: "#fff", border: "none", borderRadius: "5px" }}>Sair</button>
                </div>

                <section style={{ marginTop: "30px", background: "#f4ebf7", padding: "20px", borderRadius: "10px" }}>
                    <h3>Registrar Nova Ocorrência</h3>
                    
                    {erroAlunos && <p style={{ color: "red", fontSize: "14px", marginBottom: "10px" }}>{erroAlunos}</p>}
                    
                    <form onSubmit={handleCriarOcorrencia} style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "15px", marginTop: "15px" }}>
                        <input 
                            type="text" 
                            placeholder="Título da Ocorrência" 
                            value={novaOcorrencia.titulo}
                            onChange={(e) => setNovaOcorrencia({...novaOcorrencia, titulo: e.target.value})}
                            required
                            style={{ padding: "10px", borderRadius: "5px", border: "1px solid #ccc" }}
                        />
                        <select 
                            value={novaOcorrencia.idAluno}
                            onChange={(e) => setNovaOcorrencia({...novaOcorrencia, idAluno: e.target.value})}
                            required
                            style={{ padding: "10px", borderRadius: "5px", border: "1px solid #ccc" }}
                        >
                            <option value="">Selecione um Aluno</option>
                            {alunos.map(a => (
                                <option key={a.idAluno} value={a.idAluno}>{a.nomeAluno}</option>
                            ))}
                        </select>
                        <input 
                            type="date" 
                            value={novaOcorrencia.data}
                            onChange={(e) => setNovaOcorrencia({...novaOcorrencia, data: e.target.value})}
                            required
                            style={{ padding: "10px", borderRadius: "5px", border: "1px solid #ccc" }}
                        />
                        <select 
                            value={novaOcorrencia.categoria}
                            onChange={(e) => setNovaOcorrencia({...novaOcorrencia, categoria: e.target.value})}
                            style={{ padding: "10px", borderRadius: "5px", border: "1px solid #ccc" }}
                        >
                            <option value="Pedagógica">Pedagógica</option>
                            <option value="Comportamental">Comportamental</option>
                            <option value="Atenção">Atenção</option>
                        </select>
                        <textarea 
                            placeholder="Descrição detalhada..." 
                            value={novaOcorrencia.descricao}
                            onChange={(e) => setNovaOcorrencia({...novaOcorrencia, descricao: e.target.value})}
                            required
                            style={{ gridColumn: "1 / -1", padding: "10px", borderRadius: "5px", border: "1px solid #ccc", minHeight: "80px" }}
                        />
                        <button type="submit" style={{ gridColumn: "1 / -1", padding: "12px", background: "#7252ac", color: "#fff", border: "none", borderRadius: "5px", cursor: "pointer", fontWeight: "bold" }}>
                            Salvar Ocorrência
                        </button>
                    </form>
                    {mensagemOcorrencia && <p style={{ marginTop: "10px", color: mensagemOcorrencia.includes("Erro") ? "red" : "green" }}>{mensagemOcorrencia}</p>}
                </section>

                <section style={{ marginTop: "40px" }}>
                    <h3>Ocorrências Registradas</h3>
                    
                    {recomendacao && (
                        <div style={{ background: "#e8d9ff", padding: "20px", borderRadius: "10px", marginTop: "20px", borderLeft: "5px solid #7252ac" }}>
                            <h4 style={{ margin: 0, color: "#4f3784" }}>✨ Recomendação da Inteligência Artificial:</h4>
                            <p style={{ marginTop: "10px", fontSize: "15px", lineHeight: "1.6" }}>{recomendacao}</p>
                        </div>
                    )}

                    <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))", gap: "20px", marginTop: "20px" }}>
                        {ocorrencias.map(oc => (
                            <div key={oc.idOcorrencia} style={{ background: "#fff", border: "1px solid #ddd", padding: "20px", borderRadius: "10px", boxShadow: "0 4px 6px rgba(0,0,0,0.05)" }}>
                                <span style={{ background: "#f0eaf1", padding: "4px 10px", borderRadius: "15px", fontSize: "12px", fontWeight: "bold", color: "#7252ac" }}>{oc.categoria}</span>
                                <h4 style={{ marginTop: "15px" }}>{oc.titulo}</h4>
                                <p style={{ fontSize: "12px", color: "#666" }}>Data: {oc.data}</p>
                                <p style={{ fontSize: "14px", color: "#333", marginTop: "10px" }}>{oc.descricao}</p>
                                
                                <button 
                                    onClick={() => gerarRecomendacao(oc.idOcorrencia)}
                                    disabled={carregandoIA}
                                    style={{ width: "100%", marginTop: "15px", padding: "10px", background: "#27103f", color: "#fff", border: "none", borderRadius: "5px", cursor: "pointer", fontWeight: "bold" }}
                                >
                                    {carregandoIA ? "Analisando..." : "Gerar Plano com IA"}
                                </button>
                            </div>
                        ))}
                        {ocorrencias.length === 0 && <p>Nenhuma ocorrência registrada ainda.</p>}
                    </div>
                </section>
            </main>
        </div>
    );
}