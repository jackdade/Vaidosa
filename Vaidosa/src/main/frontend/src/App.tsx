import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import PessoaList from './screns/PessoaList';
import PessoaForm from './screns/PessoaForm';

import './styles/App.css';

function App() {
  return (
    <Router>
      <Routes>

        <Route path="/" element={<PessoaList />} />
        <Route path="/pessoas/adicionar" element={<PessoaForm />} />

      </Routes>
    </Router>
  );
}

export default App;
