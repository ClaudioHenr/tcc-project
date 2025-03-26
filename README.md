
## Principais clausulas usadas em bancos de dados

| **Cláusula**       | **H2 (modo default)** | **H2 (modo MySQL)** | **H2 (modo PostgreSQL)** | **H2 (modo Oracle)** | **H2 (modo SQL Server)** | **PostgreSQL** | **MySQL** | **Oracle** | **SQL Server** |
| ------------------ | --------------------- | ------------------- | ------------------------ | -------------------- | ------------------------ | -------------- | --------- | ---------- | -------------- |
| **`SELECT`**       | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`WHERE`**        | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`INSERT INTO`**  | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`UPDATE`**       | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`DELETE`**       | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`ORDER BY`**     | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`GROUP BY`**     | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`HAVING`**       | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`JOIN`**         | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`DISTINCT`**     | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`LIKE`**         | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`IN`**           | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`BETWEEN`**      | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`LIMIT`**        | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ❌          | ✅              |
| **`FETCH`**        | ❌                     | ❌                   | ❌                        | ✅                    | ❌                        | ✅              | ❌         | ✅          | ✅              |
| **`CASE`**         | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`ALTER TABLE`**  | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`CREATE TABLE`** | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`DROP TABLE`**   | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`TRUNCATE`**     | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |
| **`UNION`**        | ✅                     | ✅                   | ✅                        | ✅                    | ✅                        | ✅              | ✅         | ✅          | ✅              |

### Observações:

- **`LIMIT`**: Suportado no modo MySQL e no modo padrão do H2, mas **não é suportado no modo PostgreSQL** do H2.
    
- **`FETCH`**: Disponível apenas no **modo Oracle** e **SQL Server** do H2, enquanto outros bancos de dados usam `LIMIT` ou têm sua própria forma de limitar o número de linhas.
    
- **`DISTINCT`**, **`CASE`**, **`JOIN`** e outras cláusulas mais comuns são amplamente suportadas em todos os modos.
