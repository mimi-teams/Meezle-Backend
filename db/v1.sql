create table blocked_jwt (
                             jwt_token varchar(255) not null,
                             created_date datetime(6) not null,
                             last_modified_date datetime(6) not null,
                             expired_date datetime(6) not null,
                             primary key (jwt_token)
) engine=InnoDB;

create table oauth2_token(
    oauth2_token_id BINARY(16) not null,
    created_date datetime(6) not null,
    last_modified_at datetime(6) not null,
    user_id BINARY(16) not null,
    access_token TEXT not null,
    access_token_expires datetime(6) not null,
    refresh_token TEXT not null,
    refresh_token_expires datetime(6) not null,
    platform VARCHAR(10) not null,
    primary key (oauth2_token_id)
) engine=InnoDB;

create table platform_calendar(
    calendar_id BINARY(16) not null,
    created_date datetime(6) not null,
    last_modified_at datetime(6) not null,
    event_id BINARY(16) not null,
    user_id BINARY(16) not null,
    platform VARCHAR(10) not null,
    platform_calendar_id TEXT null,
    platform_event_id TEXT null,
    primary key (calendar_id)
) engine=InnoDB;

create table event (
                       event_id BINARY(16) not null,
                       created_date datetime(6) not null,
                       last_modified_date datetime(6) not null,
                       color varchar(100) not null comment '이벤트의 대표 색상. Backend에서 설정해 Front에 전달한다(브라우저마다 동일하게 보이게 만들려고!)',
                       d_day datetime(6) comment '만료 일자, 만료 일자가 없으면 null',
                       deleted_at datetime(6) comment '삭제 일자, 삭제가 안되었으면 null',
                       description VARCHAR(1000) not null comment '이벤트 세부 설명(Default : EMPTY STRING)',
                       title VARCHAR(200) not null comment '이벤트 제목. 최대 길이는 200',
                       host_id BINARY(16) not null comment '이벤트를 생성한 사용자',
                       primary key (event_id)
) engine=InnoDB;


create table event_participant (
                                   event_participant_id BINARY(16) not null,
                                   event_id BINARY(16) not null comment '연관된 event',
                                   guest_id BINARY(16) comment '참여자의 정보(user)',
                                   user_id BINARY(16) comment '참여자의 정보(user)',
                                   primary key (event_participant_id)
) engine=InnoDB;


create table event_participant_able_time (
                                             event_selectable_participle_time_id BINARY(16) not null,
                                             created_date datetime(6) not null,
                                             last_modified_date datetime(6) not null,
                                             time_ranges varchar(255) not null comment '삭제 일자, 삭제가 안되었으면 null',
                                             week integer comment '삭제 일자, 삭제가 안되었으면 null',
                                             event_participant_id BINARY(16) not null comment '연관된 EventParticipant',
                                             primary key (event_selectable_participle_time_id)
) engine=InnoDB;


create table event_selectable_participle_time (
                                                  event_selectable_participle_time_id BINARY(16) not null,
                                                  created_date datetime(6) not null,
                                                  last_modified_date datetime(6) not null,
                                                  time_ranges varchar(255) not null comment '삭제 일자, 삭제가 안되었으면 null',
                                                  week integer comment '삭제 일자, 삭제가 안되었으면 null',
                                                  event_id BINARY(16) not null comment '연관된 event',
                                                  primary key (event_selectable_participle_time_id)
) engine=InnoDB;


create table guest (
                       guest_id BINARY(16) not null,
                       created_date datetime(6) not null,
                       last_modified_date datetime(6) not null,
                       name VARCHAR(20) not null comment '참여자의 이름',
                       password varchar(255) comment '참여자 비밀번호(없어도 가능!)',
                       event_id BINARY(16) not null comment '연관된 event',
                       primary key (guest_id)
) engine=InnoDB;


create table user (
                      user_id BINARY(16) not null,
                      created_date datetime(6) not null,
                      last_modified_date datetime(6) not null,
                      deleted_at datetime(6) comment '이용자 삭제일(없으면 null)',
                      email varchar(200) not null comment '가입한 사용자 이메일(oauth login 에 사용(중복X)',
                      name varchar(200) not null comment '가입한 사용자 이름(중복O)',
                      primary key (user_id)
) engine=InnoDB;


alter table event_participant
    add constraint UK1kliqx2417x7x9yeqmdrskmu6 unique (event_id, user_id);


alter table event_participant
    add constraint UKph6rsx6dxu28xfccqtfkoy322 unique (event_id, guest_id);


alter table guest
    add constraint UKfg4coort1cbq17h2nn23vxpfx unique (name, event_id);


alter table user
    add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);


alter table event
    add constraint FKo6p2vxxycohe4dau6qni5tldf
        foreign key (host_id)
            references user (user_id);


alter table event_participant
    add constraint FK5hxneasi6gucfdlc8690c1ngc
        foreign key (event_id)
            references event (event_id);


alter table event_participant
    add constraint FKbb4i6qmdr9vvjfg7ab07l5qnn
        foreign key (guest_id)
            references guest (guest_id);


alter table event_participant
    add constraint FKhwcglexuoexhrbe9728pn6jqb
        foreign key (user_id)
            references user (user_id);


alter table event_participant_able_time
    add constraint FK3tvktrnvm644qv7yg4kdbqk2c
        foreign key (event_participant_id)
            references event_participant (event_participant_id);


alter table event_selectable_participle_time
    add constraint FK8j1i1m645ueibhdnau0oi45ld
        foreign key (event_id)
            references event (event_id);


alter table guest
    add constraint FKplwm15gu4q6tj4g4ox6wkf1li
        foreign key (event_id)
            references event (event_id);

alter table oauth2_token
    add constraint
        foreign key (user_id)
            references user (user_id);

alter table platform_calendar
    add constraint
        foreign key (user_id)
            references user (user_id);

alter table platform_calendar
    add constraint
        foreign key (event_id)
            references event (event_id);