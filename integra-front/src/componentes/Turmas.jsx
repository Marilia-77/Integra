import "./Turmas.css";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import NavbarDocente from "./NavbarDocente";
import api from "../services/api";

export default function Turmas() {
	const [professor, setProfessor] = useState(null);
	const [ocorrencias, setOcorrencias] = useState([]);
	const [recomendacao, setRecomendacao] = useState("");
	const [carregandoIA, setCarregandoIA] = useState(false);

	useEffect(() => {
		async function carregarDados() {
			try {
				const resProf = await api.get("/professores/me");
				setProfessor(resProf.data);
				const resOcorrencias = await api.get(`/ocorrencia/professor/${resProf.data.idProfessor}`);
				setOcorrencias(resOcorrencias.data);
			} catch (error) { console.error(error); }
		}
		carregarDados();
	}, []);

	async function gerarRecomendacao(idOcorrencia) {
		setRecomendacao("");
		setCarregandoIA(true);
		try {
			const response = await api.post(`/recomendacoes/gerar/${idOcorrencia}`);
			setRecomendacao(response.data.texto);
		} catch (error) {
			setRecomendacao("Erro ao gerar recomendação.");
		} finally {
			setCarregandoIA(false);
		}
	}

	return (
		<div className="turmas-pagina">
			<NavbarDocente ativa="turmas" nomeProfessor={professor?.nomeProfessor || "Professor"} />
			<div className="turmas-layout">
				<aside className="turmas-sidebar">
					<nav className="turmas-menu-lateral" aria-label="Menu do docente">
						<Link to="/portal-docente"><span aria-hidden="true">▦</span> Visão Geral</Link>
						<Link className="turmas-menu-ativa" to="/turmas"><span aria-hidden="true">▣</span> Minhas Ocorrências</Link>
					</nav>
				</aside>
				<main className="turmas-conteudo">
					<section className="turmas-cabecalho-materia">
						<div className="turmas-titulo-materia">
							<h1>Lista de Ocorrências</h1>
						</div>
					</section>
					{recomendacao && (
						<div style={{ background: "#e8d9ff", padding: "20px", borderRadius: "10px", marginTop: "20px" }}>
							<h4>✨ Plano da IA:</h4>
							<p>{recomendacao}</p>
						</div>
					)}
					<div className="turmas-grade-alunos" style={{ marginTop: "20px" }}>
						{ocorrencias.map(oc => (
							<article className="turmas-card-aluno" key={oc.idOcorrencia}>
								<h3>{oc.titulo}</h3>
								<p>{oc.descricao}</p>
								<button onClick={() => gerarRecomendacao(oc.idOcorrencia)} disabled={carregandoIA} style={{ marginTop: "10px", width: "100%", padding: "8px", background: "#7252ac", color: "#fff", border: "none", borderRadius: "5px" }}>
									{carregandoIA ? "Analisando..." : "Gerar Plano com IA"}
								</button>
							</article>
						))}
					</div>
				</main>
			</div>
		</div>
	);
}