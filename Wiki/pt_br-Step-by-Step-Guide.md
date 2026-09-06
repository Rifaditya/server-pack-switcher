# Guia Passo a Passo

Boas-vindas! Este guia foi feito para ser **extremamente fácil** de acompanhar. Mesmo que você nunca tenha administrado um servidor antes, você conseguirá configurar tudo tranquilamente!

---

## 🎮 O que este mod faz?
Normalmente, um servidor de Minecraft só pode enviar um único pacote de recursos que todos são obrigados a usar.

Com este mod, **os jogadores podem escolher seu próprio visual através de comandos!**
Por exemplo, um jogador pode digitar `/pack plastic` para usar um pacote com textura de plástico, ou `/pack pop` para retornar ao visual padrão.

Aqui está o passo a passo em **5 etapas fáceis**!

---

## 📅 Etapa 1: Obter o link do Pacote de Recursos
Para enviar o pacote ao jogador, o servidor precisa saber onde baixar o arquivo `.zip`.

### Opção A: Usando o Modrinth (A mais fácil e recomendada!)
Você não precisa de links diretos nem de CDNs para o Modrinth! Basta usar a URL do projeto, o slug ou o link de uma versão:
- **URL do projeto**: `https://modrinth.com/resourcepack/glass-doors`
- **Slug**: `glass-doors`
- **URL da versão**: `https://modrinth.com/resourcepack/glass-doors/version/1.2.3`

### Opção B: Usando o Discord
1. Envie o arquivo `.zip` do pacote para qualquer canal no Discord.
2. Clique com o **botão direito** no ícone de download do arquivo.
3. Clique em **Copiar link**.
4. O link copiado terá o formato: `https://cdn.discordapp.com/attachments/.../pack.zip` (este é o seu link direto!).

### Opção C: Usando o Dropbox
1. Envie o arquivo `.zip` para o Dropbox.
2. Clique no botão **Compartilhar** e copie o link.
3. O link será assim: `https://www.dropbox.com/s/.../pack.zip?dl=0`.
4. Mude o final do link de `?dl=0` para `?raw=1`.
5. O novo link direto será: `https://www.dropbox.com/s/.../pack.zip?raw=1`.

---

## 🔑 Etapa 2: Obter a impressão digital do pacote (Hash SHA-1)
O servidor precisa de um código de 40 dígitos (**Hash SHA-1**) para avisar o cliente do jogador: *«Ei, você já baixou esse pacote, não precisa baixar de novo!»*

### Opção A: Usando o Modrinth (Sem necessidade de Hash!)
Se você escolheu a **Opção A (Modrinth)** na Etapa 1, **você não precisa fazer isso!** O servidor buscará o hash automaticamente na API do Modrinth. Vá direto para a Etapa 3.

### Opção B: Usando um site
1. Acesse [https://emn178.github.io/online-tools/sha1_checksum.html](https://emn178.github.io/online-tools/sha1_checksum.html).
2. Arraste e solte o arquivo `.zip` na caixa do site.
3. O site exibirá imediatamente o código de 40 dígitos (ex: `da39a3ee5e6b4b0d3255bfef95601890afd80709`).
4. **Copie** esse código!

### Opção C: Usando o Windows PowerShell (Sem internet)
1. Pressione a **tecla Windows**, digite **PowerShell** e aperte **Enter**.
2. Digite `Get-FileHash -Algorithm SHA1 ` (com um espaço no final).
3. Arraste o arquivo `.zip` para dentro da janela do PowerShell.
4. Aperte **Enter**.
5. Copie o código exibido na coluna **Hash**.

---

## 📝 Etapa 3: Colocar o Link e Hash no arquivo de configuração
Ao iniciar seu servidor Minecraft com o mod instalado, um arquivo de configuração é criado:

1. Acesse a pasta do seu servidor Minecraft.
2. Abra a pasta **`config`**.
3. Localize o arquivo **`serverpackswitcher.json`**.
4. Clique com o botão direito, selecione **Abrir com** e escolha o **Bloco de Notas**.
5. Configure utilizando links do **Modrinth** ou **URLs Diretas**:

### Se estiver usando Modrinth:
```json
{
  "packs": {
    "freshanimations": {
      "modrinth": "https://modrinth.com/resourcepack/fresh-animations",
      "required": false,
      "prompt": "Would you like to use the Fresh Animations look?"
    }
  }
}
```
*(Não é necessário informar `url` nem `hash`! O servidor baixará as informações automaticamente).*

### Se estiver usando URLs Diretas (Discord/Dropbox):
```json
{
  "packs": {
    "plastic": {
      "url": "INSIRA_SEU_LINK_DO_DISCORD_OU_DROPBOX_AQUI",
      "hash": "INSIRA_O_CODIGO_SHA1_DE_40_DIGITOS_AQUI",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

6. Salve o arquivo (**Ctrl + S**) e feche o Bloco de Notas.

---

## 🔄 Etapa 4: Recarregar as configurações
Se o servidor já estiver ligado, não é preciso reiniciar:
1. Conecte-se ao servidor de Minecraft.
2. Garanta que você possui permissão de operador (`/op`).
3. Abra o chat com a tecla **T** ou **/**.
4. Digite **`/pack reload`** e aperte **Enter**.
5. Uma mensagem aparecerá confirmando o carregamento dos pacotes!

---

## 🧱 Etapa 5: Como usar os comandos no Minecraft

Qualquer jogador no servidor pode utilizar estes comandos no chat:

### 1. Ver todos os pacotes disponíveis
Digite: **`/pack list`**  
*Lista todos os nomes dos pacotes cadastrados na Etapa 3.*

### 2. Aplicar um pacote em você
Digite: **`/pack glassdoors`** (ou `/pack apply glassdoors`)  
*Uma tela aparecerá perguntando se deseja baixar e instalar o pacote. Clique em **Sim**!*

### 3. Remover o pacote ativo
Digite: **`/pack pop`** (ou `/pack clear`)  
*Seu jogo voltará imediatamente ao visual vanilla padrão.*

### 4. Comando de Admin: Enviar pacote para outro jogador
Administradores podem enviar pacotes para outros jogadores:  
Digite: **`/pack apply glassdoors Bob`** (substituindo Bob pelo nome do jogador, ou `@a` para todos).

---

Prontinho! Agora os jogadores podem personalizar o visual do jogo quando quiserem!
