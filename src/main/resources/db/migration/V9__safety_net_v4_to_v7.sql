-- Safety net: garante que todas as alterações de V4-V7 existam
-- (Flyway pode ter marcado V4-V7 como aplicadas sem executá-las)

-- V4: campos PRO de tema em profiles
ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS border_color VARCHAR(7)  NOT NULL DEFAULT '#D4AF37',
    ADD COLUMN IF NOT EXISTS shadow_style VARCHAR(20) NOT NULL DEFAULT 'NONE';

-- V5: tabela de leads
CREATE TABLE IF NOT EXISTS leads (
    id            UUID        NOT NULL DEFAULT gen_random_uuid(),
    profile_id    UUID        NOT NULL,
    widget_id     UUID        NOT NULL,
    widget_title  VARCHAR(255),
    fields_json   JSONB       NOT NULL DEFAULT '{}',
    captured_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_leads            PRIMARY KEY (id),
    CONSTRAINT fk_leads_profile    FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_leads_profile_id  ON leads (profile_id);
CREATE INDEX IF NOT EXISTS idx_leads_captured_at ON leads (profile_id, captured_at DESC);

-- V6: avatar em profiles
ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(500);

-- V7: campos SEO em profiles
ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS bio             VARCHAR(160),
    ADD COLUMN IF NOT EXISTS seo_title       VARCHAR(60),
    ADD COLUMN IF NOT EXISTS seo_description VARCHAR(160);
