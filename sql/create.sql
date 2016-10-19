CREATE TABLE tgi_tag (
  name char(100) not null,
  bitmapsize bigint not null,
  bitmap mediumblob DEFAULT NULL,
  createtime timestamp not null default current_timestamp,
  PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE tgi_tgi (
  id int NOT NULL auto_increment,
  name char(100) not null,
  expression VARCHAR(1000) not null,
  bitmapsize bigint not null,
  bitmap mediumblob DEFAULT NULL,
  isend bit DEFAULT 0,
  tgi DOUBLE,
  createtime timestamp not null default current_timestamp,
  updatetime timestamp,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

