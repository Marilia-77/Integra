import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Cadastro.css";

export default function Cadastro() {
  const navigate = useNavigate();
  const [mensagem, setMensagem] = useState({ tipo: "", texto: "" });

  const [aluno, setAluno] = useState({
    nomeAluno: "",
    emailAluno: "",
    senhaAluno: ""
  });

  const [docente, setDocente] = useState({
    nomeProfessor: "",
    emailProfessor: "",
    senhaProfessor: ""
  });

  async function cadastrarAluno(e) {
    e.preventDefault();
    setMensagem({ tipo: "", texto: "" });
    try {
      await api.post("/aluno", {
        ...aluno,
        diagnosticoAluno: null,
        idTurma: null
      });
      setMensagem({ tipo: "sucesso", texto: "Aluno cadastrado com sucesso!" });
      setAluno({ nomeAluno: "", emailAluno: "", senhaAluno: "" });
    } catch (error) {
      setMensagem({ tipo: "erro", texto: error.response?.data?.mensagem || "Erro ao cadastrar aluno." });
    }
  }

  async function cadastrarDocente(e) {
    e.preventDefault();
    setMensagem({ tipo: "", texto: "" });
    try {
      await api.post("/professores", {
        ...docente,
        idMateria: null
      });
      setMensagem({ tipo: "sucesso", texto: "Docente cadastrado com sucesso!" });
      setDocente({ nomeProfessor: "", emailProfessor: "", senhaProfessor: "" });
    } catch (error) {
      setMensagem({ tipo: "erro", texto: error.response?.data?.mensagem || "Erro ao cadastrar docente." });
    }
  }

  return (
    <>
      <section className="PaginaCadastro">
        <header>
          <h1 className="titulo">INTEGRA</h1>
          <p className="subtitulo">Portal do Aluno & Docente</p>
          <div className="iconIntegra"></div>
        </header>

        <section className="cadastro">
          <h1 className="tituloCadastreSe">Cadastre-se:</h1>
          
          {mensagem.texto && (
            <p style={{ color: mensagem.tipo === "sucesso" ? "green" : "red", textAlign: "center", marginBottom: "10px" }}>
              {mensagem.texto}
            </p>
          )}

          <div className="container">
            <div className="box">
              <div className="boxTopo">
                <h2 className="tituloBox">Aluno</h2>
                <p className="subtituloBox">
                  Inicie seu percurso acadêmico no Integra.
                </p>
              </div>
              
              <form className="boxBaixo" onSubmit={cadastrarAluno}>
                <div className="inputs">
                  <div className="tituloInput">Nome Completo</div>
                  <input
                    type="text"
                    placeholder="Ex: Bianca Silva Santos"
                    className="nome"
                    value={aluno.nomeAluno}
                    onChange={(e) => setAluno({ ...aluno, nomeAluno: e.target.value })}
                    required
                  />  
                </div>
                <div className="inputs">
                  <div className="tituloInput">E-mail Institucional</div>
                  <input
                    type="email"
                    placeholder="nome.sobrenome@aluno.senai.br"
                    className="email"
                    value={aluno.emailAluno}
                    onChange={(e) => setAluno({ ...aluno, emailAluno: e.target.value })}
                    required
                  />
                </div>
                <div className="inputs">
                  <div className="tituloInput">Senha</div>
                  <input 
                    type="password" 
                    placeholder="Mínimo 6 caracteres" 
                    className="senha" 
                    value={aluno.senhaAluno}
                    onChange={(e) => setAluno({ ...aluno, senhaAluno: e.target.value })}
                    required
                    minLength={6}
                  />
                </div>
                <button type="submit" className="botaoCadastro">Cadastrar como Aluno →</button>
              </form>
            </div>

            <div className="box">
              <div className="boxTopo">
                <h2 className="tituloBox">Docente</h2>
                <p className="subtituloBox">
                  Acesso administrativo e pedagógico para professores.
                </p>
              </div>

              <form className="boxBaixo" onSubmit={cadastrarDocente}>
                <div className="inputs">
                  <div className="tituloInput">Nome Completo</div>
                  <input
                    type="text"
                    placeholder="Ex: Prof. Emerson Rodriguez"
                    className="nome"
                    value={docente.nomeProfessor}
                    onChange={(e) => setDocente({ ...docente, nomeProfessor: e.target.value })}
                    required
                  />
                </div>
                <div className="inputs">
                  <div className="tituloInput">E-mail Corporativo (@senai)</div>
                  <input
                    type="email"
                    placeholder="nome.sobrenome@senai.br"
                    className="email"
                    value={docente.emailProfessor}
                    onChange={(e) => setDocente({ ...docente, emailProfessor: e.target.value })}
                    required
                  />
                </div>
                <div className="inputs">
                  <div className="tituloInput">Senha</div>
                  <input
                    type="password"
                    placeholder="Mínimo 6 caracteres"
                    className="senha"
                    value={docente.senhaProfessor}
                    onChange={(e) => setDocente({ ...docente, senhaProfessor: e.target.value })}
                    required
                    minLength={6}
                  />
                </div>
                <button type="submit" className="botaoCadastro">Cadastrar como Docente →</button>
              </form>
            </div>
          </div>

          <footer>
              Já possui uma conta?{" "}
              <span onClick={() => navigate("/login")}>Fazer Login</span>
            </footer>
        </section>
      </section>
    </>
  );
}