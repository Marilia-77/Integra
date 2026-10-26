import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

export default function AdminDashboard() {
    const navigate = useNavigate();
    const [materia, setMateria] = useState({ nomeMateria: "" });
    const [turma, setTurma] = useState({ ano: "", grau: "" });
    const [turmas, setTurmas] = useState([]);
    const [msgMateria, setMsgMateria] = useState("");
    const [msgTurma, setMsgTurma] = useState("");

    async function carregarTurmas() {
        try {
            const res = await api.get("/turma");
            setTurmas(res.data);
        } catch (e) { console.error(e); }
    }

    useEffect(() => { carregarTurmas(); }, []);

    async function cadastrarMateria(e) {
        e.preventDefault();
        try {
            await api.post("/materia", materia);
            setMsgMateria("Matéria cadastrada com sucesso!");
            setMateria({ nomeMateria: "" });
        } catch (error) {
            setMsgMateria("Erro ao cadastrar matéria.");
        }
    }

    async function cadastrarTurma(e) {
        e.preventDefault();
        try {
            await api.post("/turma", { ano: `${turma.ano}-01-01`, grau: Number(turma.grau) });
            setMsgTurma("Turma cadastrada com sucesso!");
            setTurma({ ano: "", grau: "" });
            carregarTurmas();
        } catch (error) {
            setMsgTurma("Erro ao cadastrar turma.");
        }
    }

    function sair() {
        localStorage.clear();
        navigate("/login");
    }

    return (
        <div style={{ padding: "40px", fontFamily: "Arial" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <h1>Painel do Administrador</h1>
                <button onClick={sair} style={{ padding: "10px", cursor: "pointer" }}>Sair</button>
            </div>
            
            <div style={{ display: "flex", gap: "40px", marginTop: "30px" }}>
                <div style={{ border: "1px solid #ccc", padding: "20px", borderRadius: "8px", width: "300px" }}>
                    <h2>Cadastrar Matéria</h2>
                    <form onSubmit={cadastrarMateria} style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                        <input type="text" placeholder="Nome da Matéria" value={materia.nomeMateria} onChange={(e) => setMateria({ nomeMateria: e.target.value })} required style={{ padding: "8px" }} />
                        <button type="submit" style={{ padding: "10px", background: "#211047", color: "#fff", border: "none", cursor: "pointer" }}>Salvar Matéria</button>
                    </form>
                    {msgMateria && <p style={{ color: msgMateria.includes("Erro") ? "red" : "green" }}>{msgMateria}</p>}
                </div>

                <div style={{ border: "1px solid #ccc", padding: "20px", borderRadius: "8px", width: "300px" }}>
                    <h2>Cadastrar Turma</h2>
                    <form onSubmit={cadastrarTurma} style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                        <input type="number" placeholder="Ano (Ex: 2026)" value={turma.ano} onChange={(e) => setTurma({ ...turma, ano: e.target.value })} required style={{ padding: "8px" }} />
                        <input type="number" step="0.1" placeholder="Grau (Ex: 5)" value={turma.grau} onChange={(e) => setTurma({ ...turma, grau: e.target.value })} required style={{ padding: "8px" }} />
                        <button type="submit" style={{ padding: "10px", background: "#211047", color: "#fff", border: "none", cursor: "pointer" }}>Salvar Turma</button>
                    </form>
                    {msgTurma && <p style={{ color: msgTurma.includes("Erro") ? "red" : "green" }}>{msgTurma}</p>}
                </div>
            </div>

            <div style={{ marginTop: "40px" }}>
                <h2>Turmas Cadastradas</h2>
                <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "10px" }}>
                    <thead>
                        <tr style={{ background: "#f0f0f0" }}>
                            <th style={{ padding: "10px", textAlign: "left" }}>ID</th>
                            <th style={{ padding: "10px", textAlign: "left" }}>Grau</th>
                            <th style={{ padding: "10px", textAlign: "left" }}>Ano</th>
                            <th style={{ padding: "10px", textAlign: "left" }}>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        {turmas.map(t => (
                            <tr key={t.idTurma} style={{ borderBottom: "1px solid #eee" }}>
                                <td style={{ padding: "10px" }}>{t.idTurma}</td>
                                <td style={{ padding: "10px" }}>{t.grau}</td>
                                <td style={{ padding: "10px" }}>{t.ano}</td>
                                <td style={{ padding: "10px" }}>
                                    <button onClick={() => navigate(`/admin/turma/${t.idTurma}`)} style={{ padding: "5px 10px", cursor: "pointer" }}>
                                        Gerenciar
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}