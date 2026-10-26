import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./PortalAluno.css";

export default function PortalAluno() {
    const navigate = useNavigate();
    const [aluno, setAluno] = useState(null);
    const [notas, setNotas] = useState([]);
    const [frequencias, setFrequencias] = useState([]);
    const [ocorrencias, setOcorrencias] = useState([]);
    const [erro, setErro] = useState("");

    useEffect(() => {
        async function carregarDados() {
            try {
                const resAluno = await api.get("/aluno/me");
                setAluno(resAluno.data);
                const idAluno = resAluno.data.idAluno;

                try {
                    const resNotas = await api.get(`/nota/aluno/${idAluno}`);
                    setNotas(resNotas.data);
                } catch (e) { console.warn("Sem notas"); }

                try {
                    const resFreq = await api.get(`/frequencia/aluno/${idAluno}`);
                    setFrequencias(resFreq.data);
                } catch (e) { console.warn("Sem frequência"); }

                // Busca as ocorrências e a IA
                try {
                    const resOcorrencias = await api.get(`/ocorrencia/aluno/${idAluno}`);
                    // Para cada ocorrência, tenta buscar a recomendação da IA
                    const ocorrenciasComIA = await Promise.all(
                        resOcorrencias.data.map(async (oc) => {
                            try {
                                const resIA = await api.get(`/recomendacoes/ocorrencia/${oc.idOcorrencia}`);
                                return { ...oc, recomendacaoIA: resIA.data.texto };
                            } catch (e) {
                                return { ...oc, recomendacaoIA: null };
                            }
                        })
                    );
                    setOcorrencias(ocorrenciasComIA);
                } catch (e) { console.warn("Sem ocorrências"); }

            } catch (error) {
                if (error.response?.status === 401 || error.response?.status === 403) {
                    localStorage.clear();
                    navigate("/login");
                } else {
                    setErro("Erro ao carregar seus dados.");
                }
            }
        }
        carregarDados();
    }, [navigate]);

    function sair() {
        localStorage.clear();
        navigate("/login");
    }

    if (erro) return <p style={{ color: "red", padding: "20px" }}>{erro}</p>;
    if (!aluno) return <p style={{ padding: "20px" }}>Carregando...</p>;

    const frequenciaMedia = frequencias.length
        ? frequencias.reduce((acc, f) => acc + f.porcentagem, 0) / frequencias.length
        : 0;

    return (
        <div className="portal-aluno">
            <header className="portal-topbar">
                <h1>Aluno 360 - Espaço do Aluno</h1>
                <div className="portal-acoes">
                    <button onClick={sair} style={{ background: "transparent", border: "none", color: "#d32f2f", cursor: "pointer", fontWeight: "bold", marginRight: "15px" }}>Sair</button>
                    <div className="portal-avatar">{aluno.nomeAluno?.charAt(0) || "A"}</div>
                </div>
            </header>

            <div className="portal-layout">
                <aside className="portal-sidebar">
                    <div className="portal-identificacao">
                        <div className="portal-icone-pessoa">♙</div>
                        <div>
                            <strong>{aluno.nomeAluno}</strong>
                            <span>{aluno.diagnosticoAluno || "Sem diagnóstico"}</span>
                        </div>
                    </div>
                    <nav>
                        <a className="portal-link-ativo" href="#visao-geral">▦ Visão Geral</a>
                    </nav>
                </aside>

                <main className="portal-conteudo" id="visao-geral">
                    <section className="portal-boas-vindas">
                        <div className="portal-boas-vindas-texto">
                            <h2>Olá, {aluno.nomeAluno?.split(" ")[0]}!</h2>
                            <p>Acompanhe seu progresso e os feedbacks dos professores.</p>
                        </div>
                        <div className="portal-indicadores">
                            <div className="portal-indicador">
                                <strong>{frequenciaMedia.toFixed(0)}%</strong>
                                <span>Frequência</span>
                            </div>
                        </div>
                    </section>

                    <div className="portal-cartoes">
                        <section className="portal-card portal-medias">
                            <div className="portal-card-titulo">
                                <span className="portal-icone portal-icone-roxo">☷</span>
                                <h2>Suas Notas</h2>
                            </div>
                            <ul>
                                {notas.length > 0 ? notas.map((nota) => (
                                    <li key={nota.idNota}>
                                        <span>Matéria {nota.idMateria}</span>
                                        <strong>{Number(nota.media).toFixed(1)}</strong>
                                    </li>
                                )) : <li>Nenhuma nota lançada.</li>}
                            </ul>
                        </section>

                        <section className="portal-card portal-frequencia">
                            <div className="portal-frequencia-cabecalho">
                                <span className="portal-icone portal-icone-vermelho">▣</span>
                            </div>
                            <h2>Frequência: {frequenciaMedia.toFixed(0)}%</h2>
                            <div className="portal-barra-frequencia">
                                <span style={{ width: `${frequenciaMedia}%` }} />
                            </div>
                        </section>
                    </div>

                    {/* ÁREA DE OCORRÊNCIAS E IA PARA O ALUNO */}
                    <div style={{ marginTop: "40px" }}>
                        <h2 style={{ fontSize: "18px", marginBottom: "15px" }}>Seus Alertas e Planos de Ação</h2>
                        
                        {ocorrencias.length === 0 ? (
                            <p style={{ color: "#666" }}>Você não tem nenhuma ocorrência registrada no momento.</p>
                        ) : (
                            <div style={{ display: "grid", gap: "15px" }}>
                                {ocorrencias.map(oc => (
                                    <div key={oc.idOcorrencia} style={{ background: "#fff", padding: "20px", borderRadius: "10px", borderLeft: "5px solid #7252ac", boxShadow: "0 2px 8px rgba(0,0,0,0.05)" }}>
                                        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                            <h3 style={{ margin: 0, fontSize: "16px" }}>{oc.titulo}</h3>
                                            <span style={{ fontSize: "12px", background: "#eee", padding: "4px 8px", borderRadius: "10px" }}>{oc.categoria}</span>
                                        </div>
                                        <p style={{ fontSize: "14px", color: "#555", marginTop: "10px" }}>{oc.descricao}</p>
                                        
                                        {oc.recomendacaoIA && (
                                            <div style={{ marginTop: "15px", background: "#f4ebf7", padding: "15px", borderRadius: "8px" }}>
                                                <strong style={{ color: "#4f3784", fontSize: "14px" }}>✨ Plano de Ação Sugerido:</strong>
                                                <p style={{ margin: "5px 0 0 0", fontSize: "14px", color: "#333" }}>{oc.recomendacaoIA}</p>
                                            </div>
                                        )}
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </main>
            </div>
        </div>
    );
}