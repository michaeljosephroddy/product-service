const form = document.getElementById("productForm");
const table = document.getElementById("productTable");
const idField = document.getElementById("productId");
const nameField = document.getElementById("name");
const priceField = document.getElementById("price");

function fetchProducts() {
  fetch("/api/products")
    .then((res) => res.json())
    .then((products) => {
      table.innerHTML = "";
      products.forEach((p) => {
        const row = document.createElement("tr");
        row.innerHTML = `
                    <td>${p.name}</td>
                    <td>€${p.price}</td>
                    <td>
                        <button onclick="editProduct(${p.id}, '${p.name}', ${p.price})">Edit</button>
                        <button onclick="deleteProduct(${p.id})">Delete</button>
                    </td>
                `;
        table.appendChild(row);
      });
    });
}

form.onsubmit = (e) => {
  e.preventDefault();
  const id = idField.value;
  const name = nameField.value;
  const price = parseFloat(priceField.value);

  const method = id ? "PUT" : "POST";
  const url = id ? `/api/products/${id}` : "/api/products";

  fetch(url, {
    method: method,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, price }),
  }).then(() => {
    form.reset();
    idField.value = "";
    fetchProducts();
  });
};

function editProduct(id, name, price) {
  idField.value = id;
  nameField.value = name;
  priceField.value = price;
}

function deleteProduct(id) {
  fetch(`/api/products/${id}`, { method: "DELETE" }).then(fetchProducts);
}

fetchProducts();
