package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TratadorAcoes implements ActionListener {
    static final String COMECAR = "COMECAR";
    static final String CARTA_0 = "CARTA_0";
    static final String CARTA_1 = "CARTA_1";
    static final String ATIVAR_COLUNA = "ATIVAR_COLUNA";
    static final String ENCERRAR_TURNO = "ENCERRAR_TURNO";
    static final String KHAN_MELHORIAS = "KHAN_MELHORIAS";
    static final String COMPRAR = "COMPRAR";
    static final String MELHORIA_GRATIS = "MELHORIA_GRATIS";
    static final String VER_CARTELA = "VER_CARTELA";
    static final String VER_CARTAS = "VER_CARTAS";
    static final String CARREGAR = "CARREGAR";
    static final String SALVAR = "SALVAR";
    static final String COMPRAR_PILHA = "COMPRAR_PILHA";
    static final String USAR_BONUS = "USAR_BONUS";
    static final String ATACAR = "ATACAR:";
    static final String OGEDEI = "OGEDEI:";
    static final String TROCAR_ESPADA = "TROCAR_ESPADA";

    @Override
    public void actionPerformed(ActionEvent e) {
        ControladorJogo c = ControladorJogo.getInstance();
        String comando = e.getActionCommand();

        if (comando.startsWith(ATACAR)) {
            String[] partes = comando.split(":");
            c.atacar(partes[1], partes[2]);
            return;
        }
        if (comando.startsWith(OGEDEI)) {
            String[] partes = comando.split(":");
            c.comprarTesouroDeOgedei(partes[1], partes[2]);
            return;
        }
        switch (comando) {
            case COMECAR:         c.comecarPartida(); break;
            case CARTA_0:         c.escolherCartaSecreta(0); break;
            case CARTA_1:         c.escolherCartaSecreta(1); break;
            case ATIVAR_COLUNA:   c.ativarColuna(); break;
            case ENCERRAR_TURNO:  c.encerrarTurno(); break;
            case KHAN_MELHORIAS:  c.usarKhanNasMelhorias(); break;
            case COMPRAR:         c.comprarMelhoria(); break;
            case MELHORIA_GRATIS: c.pegarMelhoriaGratis(); break;
            case VER_CARTELA:     c.verCartela(); break;
            case VER_CARTAS:      c.verCartas(); break;
            case CARREGAR:        c.carregarPartida(); break;
            case SALVAR:          c.salvarPartida(); break;
            case COMPRAR_PILHA:   c.comprarMelhoriaDaPilha(); break;
            case USAR_BONUS:      c.usarPecaBonus(); break;
            case TROCAR_ESPADA:   c.trocarMovimentoPorEspada(); break;
            default:              break;
        }
    }
}