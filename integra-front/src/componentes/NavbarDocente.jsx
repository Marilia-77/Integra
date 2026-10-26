import { Link } from "react-router-dom";
import "./NavbarDocente.css";

function iniciais(nome) {
    return nome.split(" ").slice(0, 2).map((parte) => parte[0]).join("").toUpperCase();
}

export default function NavbarDocente({ ativa, nomeProfessor = "Ricardo Silva" }) {
    return (
        <header className="navbar-docente">
            <Link className="navbar-docente-marca" to="/portal-docente">
                Aluno 360 - Portal do Docente
            </Link>
            <nav className="navbar-docente-links" aria-label="Navegação principal">
                <Link className={ativa === "dashboard" ? "navbar-docente-ativa" : ""} to="/portal-docente" aria-current={ativa === "dashboard" ? "page" : undefined}>
                    Dashboard
                </Link>
                <Link className={ativa === "turmas" ? "navbar-docente-ativa" : ""} to="/turmas" aria-current={ativa === "turmas" ? "page" : undefined}>
                    Turmas
                </Link>
                <Link to="/portal-docente#metricas">Desempenho</Link>
            </nav>
            <div className="navbar-docente-acoes">
                <label className="navbar-docente-busca">
                    <span aria-hidden="true">⌕</span>
                    <input type="search" placeholder="Buscar..." aria-label="Buscar" />
                </label>
                <button type="button" aria-label="Notificações" title="Notificações">♧</button>
                <button type="button" aria-label="Configurações" title="Configurações">⚙</button>
                <div className="navbar-docente-avatar" aria-label={`Perfil de ${nomeProfessor}`} title={nomeProfessor}>
                    {iniciais(nomeProfessor)}
                </div>
            </div>
        </header>
    );
}