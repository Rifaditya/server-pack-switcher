<div align="center">
  <h1>Server Pack Switcher</h1>
  <p><strong>Dynamic, player-driven server resource pack switching on demand.</strong></p>
</div>

<hr />

<p><strong>Server Pack Switcher</strong> is a server-side mod that allows players to switch between server-provided resource packs dynamically using simple chat commands.</p> 

<p>Since it runs completely on the server, players can join with unmodified vanilla clients and still customize their gameplay looks on demand!</p>

<h2>Features</h2>
<ul>
  <li><strong>Player-Driven Swapping:</strong> Players can change their own game look instantly by typing a command (e.g. <code>/pack freshanimations</code>).</li>
  <li><strong>Persistent Preferences:</strong> The server remembers each player's chosen pack. When they reconnect, the correct resource pack is automatically sent to them.</li>
  <li><strong>Direct Modrinth Integration:</strong> You don't need to manually find file URLs or calculate SHA-1 hashes! Just paste the Modrinth version URL, project slug, or version ID, and the mod resolves it asynchronously.</li>
  <li><strong>Clean Pack Swapping:</strong> Automatically pops (removes) the player's previously active pack before pushing a new one, ensuring no stacked resource packs from this mod.</li>
  <li><strong>Admin Commands:</strong> Operators can force packs on specific players or groups (e.g. <code>/pack apply plastic @a</code>) and reload configuration files live using <code>/pack reload</code>.</li>
  <li><strong>100% Server-Side:</strong> Unmodified vanilla clients can connect and benefit from this mod.</li>
</ul>

<hr />

<h2>Configuration Guide</h2>
<p>The mod automatically generates <code>config/serverpackswitcher.json</code> upon start.</p>

<h3>JSON Template</h3>
<pre><code>{
  "packs": {
    "freshanimations": {
      "modrinth": "https://modrinth.com/resourcepack/fresh-animations",
      "required": false,
      "prompt": "Would you like to apply the Fresh Animations resource pack from Modrinth?"
    },
    "plastic": {
      "url": "https://example.com/plastic_pack.zip",
      "hash": "da39a3ee5e6b4b0d3255bfef95601890afd80709",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}</code></pre>

<h3>Config Field Details</h3>
<ul>
  <li><strong><code>modrinth</code></strong> (Optional String): Modrinth project URL, version URL, or project slug. If specified, the mod queries the Modrinth API at launch to find the download link and hash code automatically.</li>
  <li><strong><code>url</code></strong> (Required if <code>modrinth</code> is omitted): Direct download link to the <code>.zip</code> file.</li>
  <li><strong><code>hash</code></strong> (Optional String): The SHA-1 hash of the zip file (40 hex characters) for client caching.</li>
  <li><strong><code>required</code></strong> (Optional Boolean, Default: <code>false</code>): If true, players cannot easily dismiss the download prompt.</li>
  <li><strong><code>prompt</code></strong> (Optional String): Custom text shown to the player when downloading.</li>
</ul>

<hr />

<h2>Commands Reference</h2>
<table border="1" cellpadding="5" cellspacing="0">
  <thead>
    <tr>
      <th>Command</th>
      <th>Permission</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>/pack list</code></td>
      <td>Everyone</td>
      <td>Lists all available configured packs.</td>
    </tr>
    <tr>
      <td><code>/pack &lt;pack_name&gt;</code></td>
      <td>Everyone</td>
      <td>Applies the specified pack to yourself.</td>
    </tr>
    <tr>
      <td><code>/pack apply &lt;pack_name&gt;</code></td>
      <td>Everyone</td>
      <td>Alias for <code>/pack &lt;pack_name&gt;</code>.</td>
    </tr>
    <tr>
      <td><code>/pack pop</code> / <code>/pack clear</code></td>
      <td>Everyone</td>
      <td>Removes your active server resource pack.</td>
    </tr>
    <tr>
      <td><code>/pack apply &lt;pack_name&gt; &lt;targets&gt;</code></td>
      <td>Operators</td>
      <td>Forces a pack onto target player(s).</td>
    </tr>
    <tr>
      <td><code>/pack pop &lt;targets&gt;</code></td>
      <td>Operators</td>
      <td>Clears packs from target player(s).</td>
    </tr>
    <tr>
      <td><code>/pack reload</code></td>
      <td>Operators</td>
      <td>Reloads configuration and refreshes Modrinth links.</td>
    </tr>
  </tbody>
</table>

<hr />

<h2>Install & Setup</h2>
<ol>
  <li>Download <code>server-pack-switcher-1.0.2+A-26.2.jar</code> and place it in your server's <code>mods</code> folder.</li>
  <li>Start the server to generate the default configuration.</li>
  <li>Edit <code>config/serverpackswitcher.json</code> to configure your packs.</li>
  <li>Run <code>/pack reload</code> to apply your new configuration live.</li>
</ol>

<hr />

<h2>Modpack Permissions</h2>
<p>You are free to include this mod in any modpack, provided it is hosted on the same platform (e.g., CurseForge). Cross-platform redistribution is not permitted.</p>
