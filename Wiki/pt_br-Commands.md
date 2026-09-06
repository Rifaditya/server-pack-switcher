# Referência de Comandos

Todos os comandos utilizam o prefixo base `/pack`.

## Lista de Comandos

| Comando | Permissão | Descrição |
| :--- | :--- | :--- |
| `/pack list` | Todos | Exibe a lista de todos os pacotes de recursos configurados no servidor. |
| `/pack <pack_name>` | Todos | Baixa e aplica o pacote de recursos especificado ao jogador. |
| `/pack apply <pack_name>` | Todos | Alias para `/pack <pack_name>`. |
| `/pack pop` | Todos | Remove o pacote do servidor ativo do cliente do jogador. |
| `/pack clear` | Todos | Alias para `/pack pop`. |
| `/pack apply <pack_name> <targets>` | Operador (Nível 2+) | Força a aplicação do pacote de recursos especificado nos alvos. |
| `/pack pop <targets>` | Operador (Nível 2+) | Força a remoção do pacote do servidor ativo dos alvos. |
| `/pack reload` | Operador (Nível 2+) | Recarrega o arquivo de configuração do disco em tempo real. |

*Nota: Os argumentos de comando oferecem preenchimento automático (tecla TAB) para todos os nomes de pacotes configurados.*

---

## Detalhes dos Comandos

### `/pack list`
Exibe os nomes de todos os pacotes de recursos configurados disponíveis para os jogadores utilizarem.

### `/pack <pack_name>`
Baixa e aplica o pacote correspondente a `<pack_name>` ao seu jogo. Se você já tiver outro pacote deste mod ativo, ele será desinstalado primeiro para uma troca limpa.

### `/pack pop` / `/pack clear`
Remove o pacote de recursos ativo do servidor do seu cliente, restaurando o visual vanilla padrão do jogo.
