CREATE TABLE words
(
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	reg_date TIMESTAMP with TIME zone not null,
	title VARCHAR(255),
	author INT not null,
	last_mod_date TIMESTAMP with TIME zone not null,
	last_mod_user INT not null,
	value VARCHAR(100) UNIQUE,
	language VARCHAR(100),
	last_ext_check TIMESTAMP with TIME zone,
	type INT NOT NULL DEFAULT 0,
	obscenity INT NOT NULL DEFAULT 0
);

CREATE TABLE collocations
(
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	reg_date TIMESTAMP with TIME zone not null,
	title VARCHAR(255),
	author INT not null,
	last_mod_date TIMESTAMP with TIME zone not null,
	last_mod_user INT not null,
	value VARCHAR(100) UNIQUE,
	language VARCHAR(100),
	type INT NOT NULL DEFAULT 0,
	obscenity INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_words_value ON words(value);
CREATE INDEX idx_words_type ON words(type);
CREATE INDEX idx_words_language ON words(language);

CREATE TABLE word_emphasis_rank_links
(
   primary_word_id uuid NOT NULL ,
   related_word_id uuid NOT NULL,
   rank INT NOT NULL DEFAULT 0,
   PRIMARY KEY (primary_word_id, related_word_id, rank),
   FOREIGN KEY(primary_word_id) REFERENCES words (id),
   FOREIGN KEY(related_word_id) REFERENCES words (id)
);

CREATE TABLE word_formality_rank_links
(
   primary_word_id uuid NOT NULL ,
   related_word_id uuid NOT NULL,
   rank INT NOT NULL DEFAULT 0,
   PRIMARY KEY (primary_word_id, related_word_id, rank),
   FOREIGN KEY(primary_word_id) REFERENCES words (id),
   FOREIGN KEY(related_word_id) REFERENCES words (id)
);

CREATE TABLE word_rls
(
     entity_id uuid NOT NULL,
     reader INT NOT NULL,
     reading_time TIMESTAMP WITH TIME ZONE,
     is_edit_allowed INT NOT NULL,
     PRIMARY KEY (entity_id, reader)
);


CREATE TABLE word_labels
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

