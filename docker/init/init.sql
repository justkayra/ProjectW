CREATE TABLE adjectives
(
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	reg_date TIMESTAMP with TIME zone not null,
	title VARCHAR(255),
	author INT not null,
	last_mod_date TIMESTAMP with TIME zone not null,
	last_mod_user INT not null,
	value VARCHAR(100) UNIQUE,
	language VARCHAR(100),
	emphasisRank INT NOT NULL DEFAULT 0,
	formalRank INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_adjectives_value ON adjectives(value);

CREATE TABLE adj_links
(
   primary_adj_id uuid NOT NULL ,
   related_adj_id uuid NOT NULL,
   PRIMARY KEY (primary_adj_id, related_adj_id),
   FOREIGN KEY(primary_adj_id) REFERENCES adjectives (id),
   FOREIGN KEY(related_adj_id) REFERENCES adjectives (id)
);

CREATE TABLE adjective_rls
(
     entity_id uuid NOT NULL,
     reader INT NOT NULL,
     reading_time TIMESTAMP WITH TIME ZONE,
     is_edit_allowed INT NOT NULL,
     PRIMARY KEY (entity_id, reader)
);


CREATE TABLE adjective_labels
(
     entity_id uuid NOT NULL,
     label_id INT NOT NULL,
     PRIMARY KEY (entity_id, label_id)
);


CREATE TABLE languages
(
	  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	  reg_date TIMESTAMP WITH TIME ZONE NOT NULL,
	  title VARCHAR(255),
	  author INT NOT NULL,
	  last_mod_date TIMESTAMP WITH TIME ZONE NOT NULL,
	  last_mod_user INT NOT NULL,
	  rank INT NOT NULL DEFAULT 999,
	  is_active BOOLEAN NOT NULL DEFAULT TRUE,
	  name VARCHAR(512) UNIQUE,
	  localized_names jsonb,
	  code VARCHAR(3)
);

CREATE TABLE labels
(
	  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	  reg_date TIMESTAMP WITH TIME ZONE NOT NULL,
	  title VARCHAR(255),
	  author INT NOT NULL,
	  last_mod_date TIMESTAMP WITH TIME ZONE NOT NULL,
	  last_mod_user INT NOT NULL,
	  rank INT NOT NULL DEFAULT 999,
	  is_active BOOLEAN NOT NULL DEFAULT TRUE,
	  name VARCHAR(512) UNIQUE,
	  localized_names jsonb,
	  category VARCHAR(512),
	  color INT NOT NULL
);

