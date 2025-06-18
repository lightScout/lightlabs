precision mediump float;

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform float u_time;
uniform float u_refractionStrength;
uniform float u_chromaticAberration;
uniform float u_glassThickness;
uniform vec3 u_glassColor;

varying vec2 v_texCoord;

// Noise function for glass surface irregularities
float random(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

// Smooth noise for glass distortion
float noise(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);
    
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));
    
    vec2 u = f * f * (3.0 - 2.0 * f);
    
    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

// Glass normal calculation for refraction
vec3 calculateGlassNormal(vec2 uv) {
    float scale = 20.0;
    float strength = 0.1;
    
    vec2 offset = vec2(0.001, 0.0);
    float heightL = noise(uv * scale - offset);
    float heightR = noise(uv * scale + offset);
    float heightD = noise(uv * scale - offset.yx);
    float heightU = noise(uv * scale + offset.yx);
    
    vec3 normal = normalize(vec3(
        (heightL - heightR) * strength,
        (heightD - heightU) * strength,
        1.0
    ));
    
    return normal;
}

// Chromatic aberration effect
vec3 chromaticAberration(sampler2D tex, vec2 uv, float strength) {
    vec2 direction = normalize(uv - 0.5);
    
    float r = texture2D(tex, uv + direction * strength * 0.01).r;
    float g = texture2D(tex, uv).g;
    float b = texture2D(tex, uv - direction * strength * 0.01).b;
    
    return vec3(r, g, b);
}

void main() {
    vec2 uv = v_texCoord;
    
    // Calculate glass surface normal
    vec3 normal = calculateGlassNormal(uv);
    
    // Apply refraction
    vec2 refractionOffset = normal.xy * u_refractionStrength * 0.05;
    vec2 refractedUV = uv + refractionOffset;
    
    // Sample background with chromatic aberration
    vec3 backgroundColor = chromaticAberration(u_texture, refractedUV, u_chromaticAberration);
    
    // Glass tint and transparency
    vec3 glassEffect = mix(backgroundColor, u_glassColor, 0.1);
    
    // Add subtle Fresnel effect
    vec3 viewDir = normalize(vec3(uv - 0.5, -1.0));
    float fresnel = pow(1.0 - max(dot(normal, -viewDir), 0.0), 2.0);
    
    // Combine effects
    vec3 finalColor = mix(glassEffect, vec3(1.0), fresnel * 0.2);
    
    // Add glass thickness simulation
    float thickness = u_glassThickness * (1.0 + noise(uv * 10.0) * 0.1);
    finalColor *= 1.0 - thickness * 0.1;
    
    gl_FragColor = vec4(finalColor, 0.9);
} 