create table if not exists tasks (
    id serial not null primary key,
    name text not null,
    description text,
    status text not null
);

create table if not exists time_records (
    id serial not null primary key,
    employee_id bigint not null,
    task_id bigint not null,
    start_time timestamp not null,
    end_time timestamp not null,
    description text,
    FOREIGN KEY (task_id) REFERENCES tasks(id)
);