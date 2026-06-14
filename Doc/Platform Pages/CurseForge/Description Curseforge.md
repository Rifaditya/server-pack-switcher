<h1>Server Pack Switcher</h1>

<p><strong>Server Pack Switcher</strong> is a server-side mod that allows players to switch between server-provided resource packs using simple commands.</p>

<h2>Features</h2>
<ul>
  <li><strong>Player-driven Swapping:</strong> Players can use <code>/pack &lt;name&gt;</code> or <code>/pack apply &lt;name&gt;</code> to download and apply any configured resource pack.</li>
  <li><strong>Persistent Preferences:</strong> Player choices are saved. When a player logs out and returns, their chosen pack is re-applied automatically.</li>
  <li><strong>Pack List:</strong> <code>/pack list</code> displays all available configured resource packs.</li>
  <li><strong>Pop/Clear Packs:</strong> <code>/pack pop</code> or <code>/pack clear</code> removes the active server resource pack.</li>
  <li><strong>Admin Control:</strong> Operators can apply/remove packs for other players and run <code>/pack reload</code> to update configurations without restarting the server.</li>
  <li><strong>100% Server-Side:</strong> Unmodified vanilla clients can connect and use this mod!</li>
</ul>

<h2>Configuration</h2>
<p>The mod generates a configuration file at <code>config/serverpackswitcher.json</code>:</p>
<pre><code>{
  "packs": {
    "plastic": {
      "url": "https://example.com/plastic_pack.zip",
      "hash": "da39a3ee5e6b4b0d3255bfef95601890afd80709",
      "required": false,
      "prompt": "Would you like to install the Plastic resource pack?"
    }
  }
}</code></pre>
<ul>
  <li><strong>url:</strong> Link to the zip file.</li>
  <li><strong>hash:</strong> (Optional) SHA-1 hash of the zip file for client caching.</li>
  <li><strong>required:</strong> (Optional) If true, the client must accept the pack.</li>
  <li><strong>prompt:</strong> (Optional) Custom text shown on the download prompt.</li>
</ul>
