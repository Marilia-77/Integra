import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./FazerLogin.css";

export default function FazerLogin() {
  const navigate = useNavigate();
  const [mensagem, setMensagem] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    setMensagem("");
    setCarregando(true);

    try {
      let response;
      let role;

      // 1. Tenta logar como ADMIN/Usuário
      try {
        response = await api.post("/usuarios/login", { email, senha });
        role = response.data.role;
      } catch (e1) {
        // 2. Se falhar, tenta como ALUNO
        try {
          response = await api.post("/aluno/login", { email, senha });
          role = "ALUNO";
        } catch (e2) {
          // 3. Se falhar, tenta como PROFESSOR
          response = await api.post("/professores/login", { email, senha });
          role = "PROFESSOR";
        }
      }

      const { token } = response.data;

      localStorage.setItem("@Integra:token", token);
      localStorage.setItem("@Integra:role", role);
      localStorage.setItem("@Integra:email", email);

      if (role === "ADMIN") {
        navigate("/admin");
      } else if (role === "PROFESSOR") {
        navigate("/portal-docente");
      } else {
        navigate("/portal-aluno");
      }

    } catch (error) {
      setMensagem("E-mail ou senha incorretos.");
    } finally {
      setCarregando(false);
    }
  }

  return (
    <main className="paginaLogin">
      <header className="cabecalhoLogin">
        <h1>INTEGRA</h1>
        <p>Portal do Aluno &amp; Docente</p>
      </header>

      <section className="conteudoLogin" aria-labelledby="tituloLogin">
        <div className="cardLogin">
          <div className="topoLogin">
            <span className="etiquetaLogin">Acesso à sua conta</span>
            <p>Entre com o e-mail e a senha cadastrados no Integra.</p>
          </div>

          <form className="formularioLogin" onSubmit={handleSubmit}>
            <label htmlFor="emailLogin">E-mail</label>
            <input
              id="emailLogin"
              type="email"
              placeholder="voce@exemplo.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <label htmlFor="senhaLogin">Senha</label>
            <input
              id="senhaLogin"
              type="password"
              placeholder="Digite sua senha"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />

            <button 
              className="botaoEntrarLogin" 
              type="submit" 
              disabled={carregando}
            >
              {carregando ? "Entrando..." : "Entrar"}
            </button>
            
            {mensagem && (
              <p className="mensagemLogin" style={{ color: "red" }} role="status">
                {mensagem}
              </p>
            )}
          </form>

          <footer className="rodapeLogin">
            Ainda não tem uma conta?{" "}
            <button type="button" onClick={() => navigate("/cadastro")}>
              Cadastre-se
            </button>
          </footer>
        </div>
      </section>
    </main>
  );
}