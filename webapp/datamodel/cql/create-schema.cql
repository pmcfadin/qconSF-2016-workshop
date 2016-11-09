use killrvideo;

CREATE TYPE address (
    street text,
    city text,
    postalcode text,
    country text,
);

CREATE TABLE user (
    email text PRIMARY KEY,
    addresses map<text,frozen<address>>,
    phone_numbers map<text, decimal>,
    joined timestamp,
    fname text,
    lname text,
    password blob,
    salt blob,
    user_id uuid
);

CREATE TABLE user_session (
    session_token timeuuid,
    user_id uuid,
    is_valid boolean,
    PRIMARY KEY (session_token)
) WITH compaction= {
  'class': 'MemoryOnlyStrategy' } AND
  compression = {'sstable_compression' : ''} AND
  caching = { 'keys':'NONE', 'rows_per_partition':'NONE' };

// KillrVideo tables

// Entity table that will store many videos for a unique user
CREATE TABLE videos (
   video_id UUID,
   user_id UUID,
   title TEXT,
   description TEXT,
   type TEXT,
   url TEXT,
   release_date TIMESTAMP,
   release_year INT,
   avg_rating FLOAT,
   mpaa_rating TEXT,
   tags SET<TEXT>,
   preview_thumbnail BLOB,
   genres SET<TEXT>,
   PRIMARY KEY ((video_id))
);

CREATE TABLE videos_by_tag (
   tag TEXT,
   video_id UUID,
   description TEXT,
   title TEXT,
   type TEXT,
   tags SET<TEXT>,
   PRIMARY KEY ((tag), video_id))
WITH CLUSTERING ORDER BY (video_id DESC);

CREATE TABLE videos_by_user (
   user_id UUID,
   video_id UUID,
   title TEXT,
   type TEXT,
   tags SET<TEXT>,
   preview_thumbnail BLOB,
   PRIMARY KEY ((user_id), video_id))
WITH CLUSTERING ORDER BY (video_id DESC);

CREATE TABLE latest_videos (
   video_bucket INT,
   video_id UUID,
   title TEXT,
   type TEXT,
   tags SET<TEXT>,
   preview_thumbnail BLOB,
   PRIMARY KEY ((video_bucket), video_id))
WITH CLUSTERING ORDER BY (video_id DESC);

CREATE TABLE actors (
  actor_name TEXT,
  appearances_ MAP<TEXT,INT>,
  videos SET<UUID>,
  PRIMARY KEY(actor_name)
);

CREATE TABLE actors_by_video (
   video_id UUID,
   actor_name TEXT,
   character_name TEXT,
   PRIMARY KEY ((video_id), actor_name, character_name)
);

// Counter table
CREATE TABLE ratings_by_video (
   video_id UUID,
   num_ratings COUNTER,
   sum_ratings COUNTER,
   PRIMARY KEY ((video_id))
);


CREATE TABLE searches_by_user (
    user_id UUID,
    search_id TIMEUUID,
    search TEXT,
    PRIMARY KEY (user_id, search_id)
);

CREATE TABLE searches_by_user_year_month (
    user_id UUID,
    year INT,
    month INT,
    search_id TIMEUUID,
    search TEXT,
    PRIMARY KEY ((user_id, year, month), search_id)
);

CREATE TABLE searches_by_user_bucketed_300 (
    user_id UUID,
    bucket INT,
    search_id TIMEUUID,
    search TEXT,
    PRIMARY KEY ((user_id, bucket), search_id)
);

