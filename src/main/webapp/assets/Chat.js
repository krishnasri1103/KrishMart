(function () {
  const css = `.kc-btn{position:fixed;right:18px;bottom:18px;z-index:999;border:0;border-radius:50%;width:54px;height:54px;font-size:24px;cursor:pointer;background:#2d5be3;color:#fff;box-shadow:0 4px 12px #0003}
  .kc-box{position:fixed;right:18px;bottom:84px;z-index:999;width:min(340px,calc(100vw - 36px));height:420px;display:none;flex-direction:column;background:#fff;border:1px solid #ddd;border-radius:12px;box-shadow:0 8px 24px #0003;overflow:hidden;font:14px system-ui,sans-serif;color:#222}
  .kc-box.open{display:flex}.kc-head{background:#2d5be3;color:#fff;padding:10px 12px;font-weight:600}
  .kc-log{flex:1;overflow-y:auto;padding:10px;display:flex;flex-direction:column;gap:8px}
  .kc-m{max-width:85%;padding:8px 10px;border-radius:10px;white-space:pre-wrap;word-wrap:break-word}
  .kc-bot{background:#eef1f7;align-self:flex-start}.kc-me{background:#2d5be3;color:#fff;align-self:flex-end}
  .kc-card{display:block;background:#fff;border:1px solid #ddd;border-radius:8px;padding:6px 8px;margin-top:6px;color:#222;text-decoration:none}
  .kc-form{display:flex;border-top:1px solid #ddd}.kc-form input{flex:1;border:0;padding:10px;font-size:14px;outline:0}
  .kc-form button{border:0;background:#2d5be3;color:#fff;padding:0 14px;cursor:pointer}`;
  const style = document.createElement("style"); style.textContent = css; document.head.appendChild(style);

  function el(tag, cls, text) { const e = document.createElement(tag); if (cls) e.className = cls; if (text) e.textContent = text; return e; }
  const btn = el("button", "kc-btn", "💬"); btn.setAttribute("aria-label", "Open chat");
  const box = el("div", "kc-box"), log = el("div", "kc-log");
  const form = el("form", "kc-form"), input = el("input"), send = el("button", "", "Send");
  input.placeholder = "Ask about products or orders"; input.maxLength = 300; send.type = "submit";
  form.append(input, send); box.append(el("div", "kc-head", "KaviMart Assistant"), log, form);
  document.body.append(btn, box);

  function add(text, who, cards) {
    const m = el("div", "kc-m " + (who === "me" ? "kc-me" : "kc-bot"), text);
    (cards || []).forEach(c => {
      const a = el("a", "kc-card"); a.href = c.url;
      a.textContent = `${c.name} - ₹${Number(c.price).toFixed(2)}` + (c.stock > 0 ? "" : " (out of stock)");
      m.appendChild(a);
    });
    log.appendChild(m); log.scrollTop = log.scrollHeight;
  }
  btn.addEventListener("click", () => {
    box.classList.toggle("open");
    if (box.classList.contains("open")) { if (!log.children.length) add("Hi! Ask me about products, prices or your orders.", "bot"); input.focus(); }
  });
  form.addEventListener("submit", async e => {
    e.preventDefault();
    const text = input.value.trim(); if (!text) return;
