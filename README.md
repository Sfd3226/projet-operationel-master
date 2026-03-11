Mini application de gestion de tâches (To‑Do) développée avec Spring Boot (API REST) et MySQL.
Architecture en couches: controller, service, repository, modèle, DTO + mapper.
Fonctionnalités: créer, modifier, supprimer, lister et marquer une tâche comme terminée.
Gestion d’erreurs centralisée et validation des entrées via DTO.

Endpoints
Base URL: http://localhost:8080/api/tasks

POST /api/tasks
GET /api/tasks
GET /api/tasks/{id}
PUT /api/tasks/{id}
PATCH /api/tasks/{id}/complete
DELETE /api/tasks/{id}