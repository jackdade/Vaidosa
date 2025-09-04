import React from 'react';
import '../styles/Confirm.css';

interface ConfirmProps {
  texto: string;
  onConfirm: () => void;
  onCancel: () => void;
}

export default function Confirm({ texto, onConfirm, onCancel }: ConfirmProps) {
  return (
    <div className="confirm-backdrop">
      <div className="confirm-box">
        <p>{texto}</p>
        <div className="confirm-buttons">
          <button className="confirm-yes" onClick={onConfirm}>Sim</button>
          <button className="confirm-no" onClick={onCancel}>Não</button>
        </div>
      </div>
    </div>
  );
}
