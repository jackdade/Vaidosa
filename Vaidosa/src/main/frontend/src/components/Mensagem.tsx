import React from 'react';
import '../styles/Mensagem.css';

interface MensagemProps {
  tipo?: 'erro' | 'sucesso';
  texto: string;
  onClose?: () => void;
}

export default function Mensagem({ tipo = 'erro', texto, onClose }: MensagemProps) {
  return (
    <div className={`mensagem ${tipo}`}>
      <span>{texto}</span>
      {onClose && <button className="close" onClick={onClose}>×</button>}
    </div>
  );
}
