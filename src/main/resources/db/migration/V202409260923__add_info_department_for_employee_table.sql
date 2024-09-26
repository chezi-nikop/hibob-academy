UPDATE employees
SET department = 'Administration'
WHERE role = 'ADMIN';

UPDATE employees
SET department = 'Management'
WHERE role = 'MANAGER';

UPDATE employees
SET department = 'Human Resources'
WHERE role = 'HR';

UPDATE employees
SET department = 'Sales'
WHERE first_name IN ('Chandler', 'Elaine');

UPDATE employees
SET department = 'Marketing'
WHERE first_name IN ('Rachel', 'George');

UPDATE employees
SET department = 'Customer Support'
WHERE first_name IN ('Phoebe', 'Cosmo', 'Gunther');

UPDATE employees
SET department = 'Product'
WHERE first_name IN ('Monica', 'David', 'Joey');

UPDATE employees
SET department = 'Operations'
WHERE department IS NULL;