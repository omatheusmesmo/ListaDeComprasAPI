CREATE TABLE list_user_permissions (
    id_list INTEGER NOT NULL,
    id_user INTEGER NOT NULL,
    permission_type VARCHAR(50) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id_list, id_user),
    FOREIGN KEY (id_list) REFERENCES lists(id_list),
    FOREIGN KEY (id_user) REFERENCES users(id_user)
);

CREATE INDEX idx_list_user_permissions_id_list ON list_user_permissions (id_list);
CREATE INDEX idx_list_user_permissions_id_user ON list_user_permissions (id_user);
CREATE INDEX idx_list_user_permissions_deleted ON list_user_permissions (deleted);