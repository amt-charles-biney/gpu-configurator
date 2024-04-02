CREATE TABLE attribute
(
    id             UUID         NOT NULL,
    attribute_name VARCHAR(255) NOT NULL,
    is_measured    BOOLEAN,
    unit           VARCHAR(255),
    description    VARCHAR(255),
    is_required    BOOLEAN,
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_attribute PRIMARY KEY (id)
);

CREATE TABLE attribute_options
(
    id                            UUID         NOT NULL,
    option_name                   VARCHAR(255) NOT NULL,
    price_adjustment              DECIMAL,
    media                         VARCHAR(255),
    brand                         VARCHAR(255),
    base_amount                   FLOAT,
    max_amount                    FLOAT,
    price_increment               DOUBLE PRECISION,
    attribute_id                  UUID,
    in_stock                      INTEGER,
    incompatible_attribute_option UUID[],
    created_at                    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at                    TIMESTAMP WITHOUT TIME ZONE,
    deleted_at                    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_attribute_options PRIMARY KEY (id)
);

CREATE TABLE brand
(
    id         UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_brand PRIMARY KEY (id)
);

CREATE TABLE carts
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_carts PRIMARY KEY (id)
);

CREATE TABLE case_incompatible_variants
(
    case_id                 UUID NOT NULL,
    incompatible_variant_id UUID NOT NULL
);

CREATE TABLE cases
(
    id              UUID         NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255),
    cover_image_url VARCHAR(255) NOT NULL,
    image_urls      TEXT[] NOT NULL,
    price           DECIMAL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_cases PRIMARY KEY (id)
);

CREATE TABLE category
(
    id            UUID         NOT NULL,
    category_name VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    thumbnail     VARCHAR(255),
    deleted_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE category_config
(
    id          UUID NOT NULL,
    category_id UUID,
    CONSTRAINT pk_categoryconfig PRIMARY KEY (id)
);

CREATE TABLE compatible_option
(
    id                  UUID NOT NULL,
    category_config_id  UUID,
    attribute_option_id UUID,
    is_compatible       BOOLEAN,
    is_measured         BOOLEAN,
    is_included         BOOLEAN,
    size                INTEGER,
    created_at          TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    deleted_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_compatibleoption PRIMARY KEY (id)
);

CREATE TABLE configuration
(
    id           UUID              NOT NULL,
    product_id   UUID,
    cart_id      UUID,
    wish_list_id UUID,
    total_price  DECIMAL           NOT NULL,
    quantity     INTEGER DEFAULT 1 NOT NULL,
    CONSTRAINT pk_configuration PRIMARY KEY (id)
);

CREATE TABLE configuration_configured_options
(
    configuration_id      UUID NOT NULL,
    configured_options_id UUID NOT NULL
);

CREATE TABLE configuration_options
(
    id           UUID NOT NULL,
    option_id    VARCHAR(255),
    option_name  VARCHAR(255),
    option_type  VARCHAR(255),
    option_price DECIMAL,
    is_included  BOOLEAN,
    is_measured  BOOLEAN,
    base_amount  DECIMAL,
    size         VARCHAR(255),
    CONSTRAINT pk_configuration_options PRIMARY KEY (id)
);

CREATE TABLE contacts
(
    id           UUID         NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    country      VARCHAR(255) NOT NULL,
    iso2code     VARCHAR(255) NOT NULL,
    dial_code    VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_contacts PRIMARY KEY (id)
);

CREATE TABLE "order"
(
    id                 UUID         NOT NULL,
    status             VARCHAR(255) NOT NULL,
    tracking_id        VARCHAR(255) NOT NULL,
    tracking_url       VARCHAR(255) NOT NULL,
    estimated_delivery VARCHAR(255),
    user_id            UUID,
    user_session       UUID,
    payment_id         UUID,
    cart_id            UUID,
    created_at         TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at         TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE otp
(
    id         UUID         NOT NULL,
    code       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    type       VARCHAR(255),
    expiration TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_otp PRIMARY KEY (id)
);

CREATE TABLE payment
(
    id         UUID         NOT NULL,
    user_id    UUID,
    ref        VARCHAR(255) NOT NULL,
    amount     DECIMAL      NOT NULL,
    channel    VARCHAR(255) NOT NULL,
    currency   VARCHAR(255) NOT NULL,
    session    VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_payment PRIMARY KEY (id)
);

CREATE TABLE payment_info
(
    id              UUID NOT NULL,
    dtype           VARCHAR(31),
    payment_type    VARCHAR(255),
    user_id         UUID NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    card_number     VARCHAR(255),
    expiration_date VARCHAR(255),
    cardholder_name VARCHAR(255),
    payment_method  VARCHAR(255),
    network         VARCHAR(255),
    phone_number    VARCHAR(255),
    iso2code        VARCHAR(255),
    dial_code       VARCHAR(255),
    country         VARCHAR(255),
    CONSTRAINT pk_payment_info PRIMARY KEY (id)
);

CREATE TABLE product
(
    id                   UUID             NOT NULL,
    product_name         VARCHAR(255)     NOT NULL,
    product_id           VARCHAR(255)     NOT NULL,
    product_description  VARCHAR(255)     NOT NULL,
    service_charge       DOUBLE PRECISION NOT NULL,
    product_price        DECIMAL          NOT NULL,
    base_config_price    DECIMAL          NOT NULL,
    case_id              UUID,
    category_id          UUID,
    product_featured     BOOLEAN,
    product_availability BOOLEAN          NOT NULL,
    created_at           TIMESTAMP WITHOUT TIME ZONE,
    updated_at           TIMESTAMP WITHOUT TIME ZONE,
    deleted_at           TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE shippings
(
    id         UUID         NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    address1   VARCHAR(255) NOT NULL,
    address2   VARCHAR(255),
    country    VARCHAR(255) NOT NULL,
    state      VARCHAR(255) NOT NULL,
    city       VARCHAR(255) NOT NULL,
    zip_code   VARCHAR(255) NOT NULL,
    email      VARCHAR(255),
    contact_id UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_shippings PRIMARY KEY (id)
);

CREATE TABLE user_sessions
(
    id                  UUID NOT NULL,
    ip_address          VARCHAR(255),
    user_agent          VARCHAR(255),
    cart_id             UUID,
    current_shipping_id UUID,
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user_sessions PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                      UUID                  NOT NULL,
    first_name              VARCHAR(255)          NOT NULL,
    last_name               VARCHAR(255)          NOT NULL,
    email                   VARCHAR(255)          NOT NULL,
    password                VARCHAR(255)          NOT NULL,
    is_verified             BOOLEAN DEFAULT FALSE NOT NULL,
    role                    VARCHAR(255),
    contact_id              UUID,
    cart_id                 UUID,
    shipping_information_id UUID,
    created_at              TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at              TIMESTAMP WITHOUT TIME ZONE,
    deleted_at              TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE wish_lists
(
    id              UUID NOT NULL,
    user_id         UUID,
    user_session_id UUID,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_wish_lists PRIMARY KEY (id)
);

ALTER TABLE attribute
    ADD CONSTRAINT uc_attribute_attribute_name UNIQUE (attribute_name);

ALTER TABLE brand
    ADD CONSTRAINT uc_brand_name UNIQUE (name);

ALTER TABLE category_config
    ADD CONSTRAINT uc_categoryconfig_category UNIQUE (category_id);

ALTER TABLE configuration_configured_options
    ADD CONSTRAINT uc_configuration_configured_options_configuredoptions UNIQUE (configured_options_id);

ALTER TABLE payment
    ADD CONSTRAINT uc_payment_ref UNIQUE (ref);

ALTER TABLE shippings
    ADD CONSTRAINT uc_shippings_contact UNIQUE (contact_id);

ALTER TABLE user_sessions
    ADD CONSTRAINT uc_user_sessions_cart UNIQUE (cart_id);

ALTER TABLE user_sessions
    ADD CONSTRAINT uc_user_sessions_current_shipping UNIQUE (current_shipping_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_cart UNIQUE (cart_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_contact UNIQUE (contact_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_shipping_information UNIQUE (shipping_information_id);

ALTER TABLE wish_lists
    ADD CONSTRAINT uc_wish_lists_user UNIQUE (user_id);

ALTER TABLE wish_lists
    ADD CONSTRAINT uc_wish_lists_user_session UNIQUE (user_session_id);

ALTER TABLE product
    ADD CONSTRAINT CASE_ID_FK FOREIGN KEY (case_id) REFERENCES cases (id);

ALTER TABLE category_config
    ADD CONSTRAINT CATEGORY_FK_ID FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE;

ALTER TABLE product
    ADD CONSTRAINT CATEGORY_ID_FK FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE;

ALTER TABLE attribute_options
    ADD CONSTRAINT FK_ATTRIBUTE_OPTIONS_ON_ATTRIBUTE FOREIGN KEY (attribute_id) REFERENCES attribute (id) ON DELETE CASCADE;

ALTER TABLE compatible_option
    ADD CONSTRAINT FK_COMPATIBLEOPTION_ON_ATTRIBUTE_OPTION FOREIGN KEY (attribute_option_id) REFERENCES attribute_options (id) ON DELETE CASCADE;

ALTER TABLE compatible_option
    ADD CONSTRAINT FK_COMPATIBLEOPTION_ON_CATEGORY_CONFIG FOREIGN KEY (category_config_id) REFERENCES category_config (id) ON DELETE CASCADE;

ALTER TABLE configuration
    ADD CONSTRAINT FK_CONFIGURATION_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

ALTER TABLE configuration
    ADD CONSTRAINT FK_CONFIGURATION_ON_WISH_LIST FOREIGN KEY (wish_list_id) REFERENCES wish_lists (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payment (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_USER_SESSION FOREIGN KEY (user_session) REFERENCES user_sessions (id);

ALTER TABLE payment_info
    ADD CONSTRAINT FK_PAYMENT_INFO_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE payment
    ADD CONSTRAINT FK_PAYMENT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE shippings
    ADD CONSTRAINT FK_SHIPPINGS_ON_CONTACT FOREIGN KEY (contact_id) REFERENCES contacts (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CONTACT FOREIGN KEY (contact_id) REFERENCES contacts (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_SHIPPING_INFORMATION FOREIGN KEY (shipping_information_id) REFERENCES shippings (id);

ALTER TABLE user_sessions
    ADD CONSTRAINT FK_USER_SESSIONS_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

ALTER TABLE user_sessions
    ADD CONSTRAINT FK_USER_SESSIONS_ON_CURRENT_SHIPPING FOREIGN KEY (current_shipping_id) REFERENCES shippings (id);

ALTER TABLE wish_lists
    ADD CONSTRAINT FK_WISH_LISTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE wish_lists
    ADD CONSTRAINT FK_WISH_LISTS_ON_USER_SESSION FOREIGN KEY (user_session_id) REFERENCES user_sessions (id);

ALTER TABLE configuration
    ADD CONSTRAINT PRODUCT_ID_FK FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE case_incompatible_variants
    ADD CONSTRAINT fk_casincvar_on_attribute_option FOREIGN KEY (incompatible_variant_id) REFERENCES attribute_options (id);

ALTER TABLE case_incompatible_variants
    ADD CONSTRAINT fk_casincvar_on_case FOREIGN KEY (case_id) REFERENCES cases (id);

ALTER TABLE configuration_configured_options
    ADD CONSTRAINT fk_conconopt_on_config_options FOREIGN KEY (configured_options_id) REFERENCES configuration_options (id);

ALTER TABLE configuration_configured_options
    ADD CONSTRAINT fk_conconopt_on_configuration FOREIGN KEY (configuration_id) REFERENCES configuration (id);