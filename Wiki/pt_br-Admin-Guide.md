# Guia do Administrador

Este guia detalha recursos administrativos, recarregamento ao vivo e persistência de preferências de jogadores.

---

## 💾 Preferências Persistentes

Quando um jogador seleciona um pacote via `/pack <name>`, o servidor grava essa escolha em `config/serverpackswitcher_preferences.json`.
- **Reaplicação Automática**: Quando o jogador sai e se reconecta mais tarde, o servidor lê automaticamente sua preferência e envia o pacote correto.
- **Limpeza de Preferências**: Se o jogador usar `/pack pop` ou `/pack clear`, sua preferência é excluída e ele não receberá nenhum pacote ao entrar novamente.

---

## 🔄 Recarregamento de Configuração ao Vivo

Se você editar o arquivo `config/serverpackswitcher.json` com o servidor em execução, não é necessário reiniciar!
1. Certifique-se de ter privilégios de operador (nível 2+).
2. Digite **`/pack reload`** no chat do jogo ou no console do servidor.
3. O servidor recarrega as configurações instantaneamente. Pacotes configurados via Modrinth são atualizados de forma assíncrona em segundo plano.

---

## 🛡️ Comandos Administrativos Direcionados

Administradores (operadores com nível de permissão 2+) podem aplicar ou remover pacotes de outros jogadores.

### Forçar a Aplicação de um Pacote
- `/pack apply plastic Bob` - Envia forçadamente o pacote `plastic` para o jogador `Bob`.
- `/pack apply plastic @a` - Envia forçadamente o pacote `plastic` para todos os jogadores online.

### Forçar a Remoção de um Pacote
- `/pack pop Bob` - Remove o pacote ativo do jogador `Bob`.
- `/pack pop @a` - Remove os pacotes ativos de todos os jogadores online.
