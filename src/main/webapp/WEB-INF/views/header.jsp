<!-- KrishBot Chat Widget -->
<div id="chat-widget" style="
    position:fixed; bottom:24px; right:24px; z-index:9999; font-family:Segoe UI,sans-serif;">

  <!-- Chat Button -->
  <button id="chat-toggle" onclick="toggleChat()" style="
      width:60px; height:60px; border-radius:50%; border:none; cursor:pointer;
      background:linear-gradient(135deg,#10b981,#ec4899);
      color:white; font-size:1.5rem; box-shadow:0 4px 20px rgba(0,0,0,0.3);">
    💬
  </button>

  <!-- Chat Box -->
  <div id="chat-box" style="
      display:none; position:absolute; bottom:70px; right:0;
      width:320px; background:white; border-radius:16px;
      box-shadow:0 10px 40px rgba(0,0,0,0.2); overflow:hidden;
      border:2px solid #e5e7eb;">

    <!-- Header -->
    <div style="background:linear-gradient(135deg,#10b981,#ec4899);
                padding:16px 18px; color:white;">
      <b style="font-size:1.1rem;">🤖 KrishBot</b>
      <span style="font-size:0.8rem; opacity:0.9; display:block;">
        Your KrishMart Assistant
      </span>
    </div>

    <!-- Messages Area -->
    <div id="chat-messages" style="
        height:280px; overflow-y:auto; padding:14px;
        background:#f9fafb; display:flex; flex-direction:column; gap:10px;">
      <div style="background:#d1fae5; color:#065f46; padding:10px 14px;
                  border-radius:10px; font-size:0.9rem; max-width:85%;">
        Hi! I'm KrishBot 🤖 How can I help you today?
      </div>
    </div>

    <!-- Input Area -->
    <div style="padding:12px; border-top:2px solid #e5e7eb;
                display:flex; gap:8px; background:white;">
      <input id="chat-input" type="text"
             placeholder="Ask about products, orders..."
             onkeydown="if(event.key==='Enter') sendChat()"
             style="flex:1; border:2px solid #e5e7eb; border-radius:8px;
                    padding:10px 12px; font-size:0.9rem; outline:none;">
      <button onclick="sendChat()" style="
          background:linear-gradient(135deg,#10b981,#ec4899);
          color:white; border:none; border-radius:8px;
          padding:10px 14px; cursor:pointer; font-weight:700;">
        ➤
      </button>
    </div>
  </div>
</div>

<script>
function toggleChat() {
  const box = document.getElementById('chat-box');
  box.style.display = box.style.display === 'none' ? 'block' : 'none';
}

async function sendChat() {
  const input = document.getElementById('chat-input');
  const messages = document.getElementById('chat-messages');
  const message = input.value.trim();
  if (!message) return;

  // Show user message
  messages.innerHTML += `
    <div style="background:#10b981;color:white;padding:10px 14px;
                border-radius:10px;font-size:0.9rem;max-width:85%;
                align-self:flex-end;margin-left:auto;">
      ${message}
    </div>`;
  input.value = '';
  messages.scrollTop = messages.scrollHeight;

  // Show typing indicator
  messages.innerHTML += `
    <div id="typing" style="background:#e5e7eb;padding:10px 14px;
                             border-radius:10px;font-size:0.85rem;
                             max-width:70%;color:#6b7280;">
      KrishBot is typing...
    </div>`;
  messages.scrollTop = messages.scrollHeight;

  try {
    const res = await fetch('/api/chat', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({message})
    });
    const data = await res.json();

    document.getElementById('typing').remove();

    messages.innerHTML += `
      <div style="background:#d1fae5;color:#065f46;padding:10px 14px;
                  border-radius:10px;font-size:0.9rem;max-width:85%;">
        🤖 ${data.reply}
      </div>`;
    messages.scrollTop = messages.scrollHeight;
  } catch (e) {
    document.getElementById('typing').remove();
    messages.innerHTML += `
      <div style="background:#fee2e2;color:#991b1b;padding:10px 14px;
                  border-radius:10px;font-size:0.9rem;">
        ❌ Error connecting. Try again!
      </div>`;
  }
}
</script>
