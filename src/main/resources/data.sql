insert into property_partner_category (id, created_at, description, is_category_deleted, type_name, updated_at)
values ('d3600789-6c10-4ba2-8ff8-1eb353458ccf', '2022-12-06 15:39:47.528411', 'Places for Off-campus only!', false, 'Off-campus Housing', '2022-12-06 15:39:47.518371')
ON CONFLICT DO NOTHING;
insert into property_partner_category (id, created_at, description, is_category_deleted, type_name, updated_at)
 values ('2f40bb3d-b2ac-4842-b3ae-b1078023500a', '2022-12-06 15:40:22.454656', 'Places for general living only!', false, 'General Living', '2022-12-06 15:40:22.442696')
 ON CONFLICT DO NOTHING;
insert into property_partner_category (id, created_at, description, is_category_deleted, type_name, updated_at)
 values ('d9e1959b-8d44-40d3-b9d4-5dde29b75612', '2022-12-06 15:40:47.161327', 'Places for co-living only!', false, 'Co-living', '2022-12-06 15:40:47.156357')
 ON CONFLICT DO NOTHING;
