const url = "http://localhost:8080/task/user/2";

function hideLoader() {
    document.getElementById("loading").style.display = "none";
}

function show(tasks) {
    let tab = `
        <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Description</th>
                <th scope="col">Username</th>
                <th scope="col">User ID</th>
            </tr>
        </thead>
        <tbody>
    `;

    for (let task of tasks) {
        tab += `
            <tr>
                <td scope="row">${task.id}</td>
                <td>${task.description}</td>
                <td>${task.user.username}</td>
                <td>${task.user.id}</td>
            </tr>
        `;
    }

    tab += `</tbody>`; // Fechando o tbody corretamente
    document.getElementById("tasks").innerHTML = tab;
}

async function getAPI(url) {
    try {
        const response = await fetch(url, { method: "GET" });

        if (!response.ok) {
            throw new Error(`Erro: ${response.status} - ${response.statusText}`);
        }

        const data = await response.json();
        hideLoader();
        show(data);
    } catch (error) {
        console.error("Erro ao buscar tarefas:", error);
    }
}

getAPI(url);
