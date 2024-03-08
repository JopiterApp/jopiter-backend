DO
$$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'period') THEN
CREATE TYPE period AS ENUM ('Lunch', 'Dinner');
END IF;
END
$$

CREATE TABLE IF NOT EXISTS restaurant_item
(
    restaurant_id   int  not null,
    restaurant_name text not null,
    date            date not null,
    period period not null,
    calories        int null default null,
    main_item       text null default null,
    vegetarian_item text null default null,
    dessert_item    text null default null,
    mundane_items   text[] not null default ARRAY[]::text[],
    unparsed_menu   text not null,

    PRIMARY KEY (restaurant_id, date, period)
);