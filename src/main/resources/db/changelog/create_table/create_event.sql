CREATE TABLE EVENT(
    id BIGINT NOT NULL,
    event_name character varying(255) NOT NULL,
    event_description character varying(500) NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp NOT NULL
);