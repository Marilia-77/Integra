import { BrowserRouter, Routes, Route } from 'react-router-dom';

import Home from "./componentes/Home";
import Cadastro from './componentes/Cadastro';
import FazerLogin from './componentes/FazerLogin';
import PortalAluno from './componentes/PortalAluno';
import PortalDocente from './componentes/PortalDocente';
import Turmas from './componentes/Turmas';
import AdminDashboard from './componentes/AdminDashboard';
import GerenciarTurma from './componentes/GerenciarTurma';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="/login" element={<FazerLogin />} />
        <Route path="/portal-aluno" element={<PortalAluno />} />
        <Route path="/portal-docente" element={<PortalDocente />} />
        <Route path="/turmas" element={<Turmas />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/admin/turma/:id" element={<GerenciarTurma />} />
        <Route path="*" element={<div style={{padding: "40px"}}>Página não encontrada. <a href="/">Voltar</a></div>} />
      </Routes>
    </BrowserRouter>
  );
}