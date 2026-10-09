async function mutateCart(action, productId, quantity) {
  const response = await fetch(`${window.contextPath}/api/cart`, {
    method: "POST", headers: {"Content-Type": "application/json"},
    body: JSON.stringify({action, productId, quantity})
  });
  const result = await response.json();
  if (!result.success) { alert(result.message || "Cart update failed."); return false; }
  const total = document.querySelector("[data-cart-total]");
  if (total) total.textContent = `₹${Number(result.data).toFixed(2)}`;
  return true;
}
document.addEventListener("click", event => {
  const button = event.target.closest("[data-add-cart]");
  if (!button) return;
  event.preventDefault();
  mutateCart("add", Number(button.dataset.addCart), 1).then(ok => {
    if (ok) button.textContent = "Added";
  });
});