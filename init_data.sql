INSERT INTO privileges_tables_grants
VALUES
       ('VENDORS_READ_PRIVILEGE', 'users', 'SELECT'),
       ('VENDORS_READ_PRIVILEGE', 'vendors_merchandises', 'SELECT'),
       ('VENDORS_READ_PRIVILEGE', 'vendors_orders', 'SELECT'),
       ('VENDORS_READ_PRIVILEGE', 'vendor_transactions', 'SELECT'),
       ('VENDORS_READ_PRIVILEGE', 'vendor_communications', 'SELECT'),
       ('VENDORS_READ_PRIVILEGE', 'vendor_communications_messages', 'SELECT'),

       ('VENDORS_WRITE_PRIVILEGE', 'users', 'SELECT'),
       ('VENDORS_WRITE_PRIVILEGE', 'users', 'UPDATE'),
       ('VENDORS_WRITE_PRIVILEGE', 'vendors_merchandises', 'ALL'),
       ('VENDORS_WRITE_PRIVILEGE', 'vendors_orders', 'ALL'),
       ('VENDORS_WRITE_PRIVILEGE', 'vendor_transactions', 'SELECT'),
       ('VENDORS_WRITE_PRIVILEGE', 'vendor_transactions', 'INSERT'),
       ('VENDORS_WRITE_PRIVILEGE', 'vendor_transactions', 'UPDATE'),
       ('VENDORS_WRITE_PRIVILEGE', 'vendor_communications', 'SELECT'),
       ('VENDORS_WRITE_PRIVILEGE', 'vendor_communications_messages', 'SELECT'),

       ('SUPPLIERS_READ_PRIVILEGE', 'suppliers_products', 'SELECT'),

       ('SUPPLIERS_WRITE_PRIVILEGE', 'suppliers_products', 'ALL'),

       ('USER_GROUPS_READ_PRIVILEGE', 'user_groups', 'SELECT'),
       ('USER_GROUPS_READ_PRIVILEGE', 'user_groups_users', 'SELECT'),

       ('USER_GROUPS_WRITE_PRIVILEGE', 'user_groups', 'ALL'),
       ('USER_GROUPS_WRITE_PRIVILEGE', 'user_groups_users', 'ALL'),

       ('USER_SEGMENTS_READ_PRIVILEGE', 'user_segments', 'SELECT'),
       ('USER_SEGMENTS_READ_PRIVILEGE', 'user_segments_users', 'SELECT'),
       ('USER_SEGMENTS_READ_PRIVILEGE', 'user_segment_filters', 'SELECT'),
       ('USER_SEGMENTS_READ_PRIVILEGE', 'user_segment_filter_values', 'SELECT'),

       ('USER_SEGMENTS_WRITE_PRIVILEGE', 'user_segments', 'ALL'),
       ('USER_SEGMENTS_WRITE_PRIVILEGE', 'user_segments_users', 'ALL'),
       ('USER_SEGMENTS_WRITE_PRIVILEGE', 'user_segment_filters', 'SELECT'),
       ('USER_SEGMENTS_WRITE_PRIVILEGE', 'user_segment_filter_values', 'SELECT'),

       ('USERS_READ_PRIVILEGE', 'users', 'SELECT'),
       ('USERS_READ_PRIVILEGE', 'user_tags', 'SELECT'),
       ('USERS_READ_PRIVILEGE', 'user_ordered_products', 'SELECT'),

       ('USERS_WRITE_PRIVILEGE', 'users', 'ALL'),
       ('USERS_WRITE_PRIVILEGE', 'user_tags', 'ALL'),
       ('USERS_WRITE_PRIVILEGE', 'user_ordered_products', 'ALL'),

       ('MESSAGES_READ_PRIVILEGE', 'dialogs', 'SELECT'),
       ('MESSAGES_READ_PRIVILEGE', 'dialogs_messages', 'SELECT'),

       ('MESSAGES_WRITE_PRIVILEGE', 'dialogs', 'ALL'),
       ('MESSAGES_WRITE_PRIVILEGE', 'dialogs_messages', 'ALL')
;