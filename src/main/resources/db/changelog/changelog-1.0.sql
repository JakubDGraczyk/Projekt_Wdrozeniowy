create table if not exists article(
    id uuid DEFAULT uuid_generate_v4() primary key,
    creation_date timestamp DEFAULT now() not null,
    title varchar(32),
    contents varchar(128),
    author varchar(32),
    exported boolean default false
    );