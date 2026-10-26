import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";

export default function GerenciarTurma() {
    const { id } = useParams();
    const navigate = useNavigate();
    
    const [turma, setTurma] = useState(null);
    const [todosAlunos, setTodosAlunos] = useState([]);
    const [professores, setProfessores] = useState([]);
    
    const [erro, setErro] = useState("");
    const [aviso, setAviso] = useState("");
    const [carregando, setCarregando] = useState(true);
    const [salvando, setSalvando] = useState(false);

    async function carregarDados() {
        setErro("");
        try {
            const resTurma = await api.get(`/turma/${id}`);
            setTurma(resTurma.data);

            const resAlunos = await api.get("/aluno");
            setTodosAlunos(resAlunos.data);

            const resProfs = await api.get("/professores");
            setProfessores(resProfs.data);

        } catch (error) {
            setErro(`Erro ao carregar turma: ${error.response?.status || error.message}`);
        } finally {
            setCarregando(false);
        }
    }

    useEffect(() => { carregarDados(); }, [id]);

    // --- AÇÕES DE ALUNO ---
    async function adicionarAluno(idAluno) {
        setAviso("");
        setSalvando(true);
        try {
            await api.post(`/turma/${id}/alunos/${idAluno}`);
            setAviso("Aluno vinculado com sucesso.");
            await carregarDados();
        } catch (error) {
            const status = error.response?.status;
            const msg = error.response?.data?.mensagem || error.message;
            setAviso(`Falha ao adicionar aluno (${status}): ${msg}`);
        } finally {
            setSalvando(false);
        }
    }

    async function removerAluno(idAluno) {
        setAviso("");
        setSalvando(true);
        try {
            await api.delete(`/turma/${id}/alunos/${idAluno}`);
            setAviso("Aluno removido da turma.");
            await carregarDados();
        } catch (error) {
            const status = error.response?.status;
            const msg = error.response?.data?.mensagem || error.message;
            setAviso(`Falha ao remover aluno (${status}): ${msg}`);
        } finally {
            setSalvando(false);
        }
    }

    // --- AÇÕES DE PROFESSOR ---
    async function adicionarProfessor(idProfessor) {
        setAviso("");
        setSalvando(true);
        try {
            await api.post(`/turma/${id}/professores/${idProfessor}`);
            setAviso("Professor vinculado com sucesso.");
            await carregarDados();
        } catch (error) {
            const status = error.response?.status;
            const msg = error.response?.data?.mensagem || error.message;
            setAviso(`Falha ao adicionar professor (${status}): ${msg}`);
        } finally {
            setSalvando(false);
        }
    }

    async function removerProfessor(idProfessor) {
        setAviso("");
        setSalvando(true);
        try {
            await api.delete(`/turma/${id}/professores/${idProfessor}`);
            setAviso("Professor removido.");
            await carregarDados();
        } catch (error) {
            const status = error.response?.status;
            const msg = error.response?.data?.mensagem || error.message;
            setAviso(`Falha ao remover professor (${status}): ${msg}`);
        } finally {
            setSalvando(false);
        }
    }

    if (carregando) return <p style={{ padding: "40px" }}>Carregando...</p>;
    if (erro) return <p style={{ color: "red", padding: "40px" }}>{erro} <br/><a href="/admin">Voltar</a></p>;
    if (!turma) return <p style={{ padding: "40px" }}>Turma não encontrada. <a href="/admin">Voltar</a></p>;

    const anoFormatado = typeof turma.ano === "string" ? turma.ano.split("-")[0] : turma.ano;
    const professoresVinculados = turma.professores || [];
    const alunosVinculados = todosAlunos.filter(a => a.idTurma === Number(id));

    return (
        <div style={{ padding: "40px", fontFamily: "Arial", maxWidth: "900px", margin: "0 auto" }}>
            <button onClick={() => navigate("/admin")} style={{ marginBottom: "20px", cursor: "pointer" }}>
                ← Voltar ao Painel
            </button>

            <h1>Gerenciar Turma {turma.grau} - {anoFormatado}</h1>

            {aviso && (
                <div style={{
                    padding: "10px", borderRadius: "5px", marginTop: "15px",
                    background: aviso.includes("Falha") ? "#f8d7da" : "#d4edda",
                    color: aviso.includes("Falha") ? "#721c24" : "#155724"
                }}>
                    {aviso}
                </div>
            )}

            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "30px", marginTop: "30px" }}>
                
                {/* COLUNA DE ALUNOS */}
                <div style={{ border: "1px solid #ccc", padding: "20px", borderRadius: "8px" }}>
                    <h2>Alunos ({alunosVinculados.length})</h2>
                    
                    {alunosVinculados.length > 0 ? (
                        <ul style={{ paddingLeft: "20px" }}>
                            {alunosVinculados.map(a => (
                                <li key={a.idAluno} style={{ marginBottom: "10px", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                    <span>{a.nomeAluno}</span>
                                    <button onClick={() => removerAluno(a.idAluno)} disabled={salvando} style={{ background: "#d32f2f", color: "#fff", border: "none", padding: "4px 8px", borderRadius: "4px", cursor: "pointer", fontSize: "12px" }}>
                                        Remover
                                    </button>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>Nenhum aluno nesta turma ainda.</p>
                    )}

                    <div style={{ marginTop: "20px" }}>
                        <h3 style={{ fontSize: "14px" }}>Adicionar Aluno:</h3>
                        <select 
                            disabled={salvando}
                            onChange={(e) => {
                                if (e.target.value) adicionarAluno(e.target.value);
                                e.target.value = "";
                            }}
                            style={{ width: "100%", padding: "8px", marginTop: "5px" }}
                        >
                            <option value="">Selecione um aluno...</option>
                            {todosAlunos
                                .filter(a => a.idTurma !== Number(id)) // Mostra só quem não está nesta turma
                                .map(a => (
                                    <option key={a.idAluno} value={a.idAluno}>
                                        {a.nomeAluno} {a.idTurma ? "(em outra turma)" : "(sem turma)"}
                                    </option>
                                ))}
                        </select>
                    </div>
                </div>

                {/* COLUNA DE PROFESSORES */}
                <div style={{ border: "1px solid #ccc", padding: "20px", borderRadius: "8px" }}>
                    <h2>Professores ({professoresVinculados.length})</h2>
                    
                    {professoresVinculados.length > 0 ? (
                        <ul style={{ paddingLeft: "20px" }}>
                            {professoresVinculados.map(p => (
                                <li key={p.idProfessor} style={{ marginBottom: "10px", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                    <span>{p.nomeProfessor}</span>
                                    <button onClick={() => removerProfessor(p.idProfessor)} disabled={salvando} style={{ background: "#d32f2f", color: "#fff", border: "none", padding: "4px 8px", borderRadius: "4px", cursor: "pointer", fontSize: "12px" }}>
                                        Remover
                                    </button>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>Nenhum professor vinculado.</p>
                    )}

                    <div style={{ marginTop: "20px" }}>
                        <h3 style={{ fontSize: "14px" }}>Adicionar Professor:</h3>
                        <select
                            disabled={salvando}
                            onChange={(e) => {
                                if (e.target.value) adicionarProfessor(e.target.value);
                                e.target.value = "";
                            }}
                            style={{ width: "100%", padding: "8px", marginTop: "5px" }}
                        >
                            <option value="">Selecione um professor...</option>
                            {professores
                                .filter(p => !professoresVinculados.some(tp => tp.idProfessor === p.idProfessor))
                                .map(p => (
                                    <option key={p.idProfessor} value={p.idProfessor}>{p.nomeProfessor}</option>
                                ))}
                        </select>
                    </div>
                </div>
            </div>
        </div>
    );
}