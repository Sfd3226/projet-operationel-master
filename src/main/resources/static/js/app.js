const apiBase = "/api/tasks";

const form = document.getElementById("task-form");
const list = document.getElementById("task-list");
const emptyState = document.getElementById("empty-state");
const refreshBtn = document.getElementById("refresh");
const jumpToFormBtn = document.getElementById("jump-to-form");

const renderTasks = (tasks) => {
    list.innerHTML = "";
    if (!tasks.length) {
        emptyState.style.display = "block";
        return;
    }
    emptyState.style.display = "none";

    tasks.forEach((task) => {
        const li = document.createElement("li");
        li.className = `task ${task.status === "COMPLETED" ? "completed" : ""}`;

        const header = document.createElement("div");
        header.className = "task-header";

        const title = document.createElement("div");
        title.className = "task-title";
        title.textContent = task.title;

        const meta = document.createElement("div");
        meta.className = "task-meta";
        meta.textContent = task.status === "COMPLETED" ? "Terminée" : "En cours";

        header.appendChild(title);
        header.appendChild(meta);

        const desc = document.createElement("div");
        desc.textContent = task.description || "";

        const actions = document.createElement("div");
        actions.className = "task-actions";

        const editBtn = document.createElement("button");
        editBtn.className = "btn-small";
        editBtn.type = "button";
        editBtn.textContent = "Modifier";

        if (task.status !== "COMPLETED") {
            const completeBtn = document.createElement("button");
            completeBtn.className = "btn-small btn-complete";
            completeBtn.type = "button";
            completeBtn.textContent = "Terminer";
            completeBtn.addEventListener("click", async () => {
                await fetch(`${apiBase}/${task.id}/complete`, { method: "PATCH" });
                await loadTasks();
            });
            actions.appendChild(completeBtn);
        }

        const deleteBtn = document.createElement("button");
        deleteBtn.className = "btn-small";
        deleteBtn.type = "button";
        deleteBtn.textContent = "Supprimer";
        deleteBtn.addEventListener("click", async () => {
            await fetch(`${apiBase}/${task.id}`, { method: "DELETE" });
            await loadTasks();
        });
        actions.appendChild(deleteBtn);

        li.appendChild(header);
        if (desc.textContent) {
            li.appendChild(desc);
        }
        li.appendChild(actions);
        list.appendChild(li);

        editBtn.addEventListener("click", () => {
            if (li.classList.contains("editing")) {
                return;
            }
            li.classList.add("editing");

            const titleInput = document.createElement("input");
            titleInput.type = "text";
            titleInput.value = task.title;
            titleInput.className = "task-input";

            const descInput = document.createElement("textarea");
            descInput.rows = 2;
            descInput.value = task.description || "";
            descInput.className = "task-input";

            const saveBtn = document.createElement("button");
            saveBtn.type = "button";
            saveBtn.className = "btn-small btn-complete";
            saveBtn.textContent = "Sauver";

            const cancelBtn = document.createElement("button");
            cancelBtn.type = "button";
            cancelBtn.className = "btn-small";
            cancelBtn.textContent = "Annuler";

            const editActions = document.createElement("div");
            editActions.className = "task-actions";
            editActions.appendChild(saveBtn);
            editActions.appendChild(cancelBtn);

            title.replaceWith(titleInput);
            if (desc.textContent) {
                desc.replaceWith(descInput);
            } else {
                li.insertBefore(descInput, actions);
            }
            actions.replaceWith(editActions);

            const exitEdit = () => {
                titleInput.replaceWith(title);
                descInput.replaceWith(desc);
                editActions.replaceWith(actions);
                li.classList.remove("editing");
            };

            cancelBtn.addEventListener("click", () => {
                exitEdit();
            });

            saveBtn.addEventListener("click", async () => {
                const nextTitle = titleInput.value.trim();
                if (!nextTitle) {
                    titleInput.focus();
                    return;
                }
                const nextDescription = descInput.value.trim();
                await fetch(`${apiBase}/${task.id}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        title: nextTitle,
                        description: nextDescription || null,
                        status: task.status,
                    }),
                });
                await loadTasks();
            });
        });

        actions.insertBefore(editBtn, actions.firstChild);
    });
};

const loadTasks = async () => {
    const response = await fetch(apiBase);
    const tasks = await response.json();
    renderTasks(tasks);
};

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const title = document.getElementById("title").value.trim();
    const description = document.getElementById("description").value.trim();

    if (!title) {
        return;
    }

    await fetch(apiBase, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            title,
            description: description || null,
            status: "IN_PROGRESS",
        }),
    });

    form.reset();
    await loadTasks();
});

refreshBtn.addEventListener("click", loadTasks);
if (jumpToFormBtn) {
    jumpToFormBtn.addEventListener("click", () => {
        form.scrollIntoView({ behavior: "smooth" });
        document.getElementById("title").focus();
    });
}

loadTasks();
