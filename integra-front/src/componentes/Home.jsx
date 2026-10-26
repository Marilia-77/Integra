import React from "react";
import { useNavigate } from 'react-router-dom';
import "./Home.css";

export default function Home() {
  const navigate = useNavigate();

  return (
    <>
      <div className="pagina">
        <header className="cabecalho">
          <div className="cabecalhoEsquerda">
            <h1 className="tituloHome">INTEGRA</h1>
            <p className="subtituloHome">Portal do Aluno & Docente</p>
            {/* 
            <nav className="navbar">
              <a href="#" className="linkAtivo">Dashboard</a>
              <a href="#">Turmas</a>
              <a href="#">Desempenho</a>
              <a href="#">Mensagens</a>
            </nav> 
            */}
          </div>
          <div className="cabecalhoDireita">
            {/* <img src="" alt="Logo Integra" className="logoFoto" /> */}
          </div>
        </header>

        <main className="conteudo">
          <section className="menu">
            <div className="textoMenu">
              <span className="tagMenu">Novo no Portal</span>
              <h2>Acompanhamento Pedagógico Centralizado</h2>
              <p>
                Cadastre-se para acessar suas turmas, relatórios de IA e o
                histórico completo dos seus alunos. Uma plataforma inteligente
                para educadores que priorizam o sucesso acadêmico.
              </p>
              <div className="botoesMenu">
                <button className="botaoCadastrar" onClick={() => navigate("/login")}>
                  Entrar
                </button>
                <button className="botaoEntrar" onClick={() => navigate("/cadastro")}>
                  Cadastrar-se
                </button>
              </div>
            </div>
            
            <div className="imagemContainer">
              <div className="imagemMenu">
                <img
                  src="https://www.dataplace.com.br/wp-content/uploads/2020/12/empresario.jpg"
                  alt="Docente"
                />
                <div className="overlayEscuro"></div>
              </div>

              <div className="cardIa">
                <div className="iconeIa">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M12 2a10 10 0 1 0 10 10H12V2z"></path>
                    <path d="M12 12L2.5 7.5"></path>
                    <path d="M12 12v10"></path>
                  </svg>
                </div>
                <div className="textoIa">
                  <h4>Portal Integra</h4>
                  <p>Conectando alunos e docentes em um só lugar</p>
                </div>
              </div>
            </div>
          </section>
        </main>
      </div>
    </>
  );
}