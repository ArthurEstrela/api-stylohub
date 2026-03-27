CREATE TABLE IF NOT EXISTS analytics_events (
    id BIGSERIAL PRIMARY KEY,
    profile_id UUID NOT NULL,
    widget_id UUID,
    event_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_analytics_profile_id ON analytics_events (profile_id);
CREATE INDEX IF NOT EXISTS idx_analytics_created_at ON analytics_events (created_at);
