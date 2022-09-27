CREATE TABLE `w2m_event_able_time` (
	`event_date_time_id`	LONG	NOT NULL,
	`event_id2`	LONG	NOT NULL,
	`created_time`	DATE	NOT NULL	DEFAULT NOW(),
	`last_modified_time`	DATE	NOT NULL	DEFAULT NOW(),
	`able_date`	DATE	NOT NULL	DEFAULT FALSE,
	`start_time`	TIME	NULL	DEFAULT FALSE,
	`end_time`	TIME	NULL	DEFAULT FALSE
);

CREATE TABLE `w2m_user` (
	`user_id`	LONG	NOT NULL,
	`created_time`	DATE	NOT NULL	DEFAULT NOW(),
	`last_modified_time`	DATE	NOT NULL	DEFAULT NOW(),
	`email`	TEXT	NULL	COMMENT '소셜 로그인 이메일',
	`name`	VARCHAR(100)	NOT NULL
);

CREATE TABLE `w2m_event` (
	`event_id`	LONG	NOT NULL,
	`user_id`	LONG	NOT NULL,
	`created_time`	DATE	NOT NULL	DEFAULT NOW(),
	`last_modified_time`	DATE	NOT NULL	DEFAULT NOW(),
	`name`	VARCHAR(100)	NOT NULL,
	`deleted_date`	DATE	NULL	DEFAULT FALSE,
	`d_day`	DATE	NULL
);

CREATE TABLE `w2m_participant` (
	`event_date_time_id`	LONG	NOT NULL,
	`event_id2`	LONG	NOT NULL,
	`created_time`	DATE	NOT NULL	DEFAULT NOW(),
	`last_modified_time`	DATE	NOT NULL	DEFAULT NOW(),
	`name`	VARCHAR(100)	NOT NULL,
	`password`	VARCHAR(100)	NULL	DEFAULT FALSE
);

CREATE TABLE `w2m_event_participatable_time` (
	`participatable_time_id`	LONG	NOT NULL,
	`event_id`	LONG	NOT NULL,
	`created_time`	DATE	NOT NULL	DEFAULT NOW(),
	`last_modified_time`	DATE	NOT NULL	DEFAULT NOW(),
	`able_date`	DATE	NOT NULL	DEFAULT FALSE,
	`start_time`	TIME	NULL	DEFAULT FALSE,
	`end_time`	TIME	NULL	DEFAULT FALSE,
	`user_id`	LONG	NOT NULL,
	`event_date_time_id2`	LONG	NOT NULL
);

ALTER TABLE `w2m_event_able_time` ADD CONSTRAINT `PK_W2M_EVENT_ABLE_TIME` PRIMARY KEY (
	`event_date_time_id`,
	`event_id2`
);

ALTER TABLE `w2m_user` ADD CONSTRAINT `PK_W2M_USER` PRIMARY KEY (
	`user_id`
);

ALTER TABLE `w2m_event` ADD CONSTRAINT `PK_W2M_EVENT` PRIMARY KEY (
	`event_id`,
	`user_id`
);

ALTER TABLE `w2m_participant` ADD CONSTRAINT `PK_W2M_PARTICIPANT` PRIMARY KEY (
	`event_date_time_id`,
	`event_id2`
);

ALTER TABLE `w2m_event_participatable_time` ADD CONSTRAINT `PK_W2M_EVENT_PARTICIPATABLE_TIME` PRIMARY KEY (
	`participatable_time_id`,
	`event_id`
);

ALTER TABLE `w2m_event_able_time` ADD CONSTRAINT `FK_w2m_event_TO_w2m_event_able_time_1` FOREIGN KEY (
	`event_id2`
)
REFERENCES `w2m_event` (
	`event_id`
);

ALTER TABLE `w2m_event` ADD CONSTRAINT `FK_w2m_user_TO_w2m_event_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `w2m_user` (
	`user_id`
);

ALTER TABLE `w2m_participant` ADD CONSTRAINT `FK_w2m_event_TO_w2m_participant_1` FOREIGN KEY (
	`event_id2`
)
REFERENCES `w2m_event` (
	`event_id`
);

ALTER TABLE `w2m_event_participatable_time` ADD CONSTRAINT `FK_w2m_event_TO_w2m_event_participatable_time_1` FOREIGN KEY (
	`event_id`
)
REFERENCES `w2m_event` (
	`event_id`
);

