precision mediump float;

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform float u_blurRadius;

varying vec2 v_texCoord;

void main() {
    vec2 texelSize = 1.0 / u_resolution;
    vec3 result = vec3(0.0);
    
    int radius = int(u_blurRadius);
    int samples = 0;
    
    // Simple box blur - very fast
    for(int x = -4; x <= 4; x++) {
        for(int y = -4; y <= 4; y++) {
            if(abs(x) <= radius && abs(y) <= radius) {
                vec2 offset = vec2(float(x), float(y)) * texelSize;
                result += texture2D(u_texture, v_texCoord + offset).rgb;
                samples++;
            }
        }
    }
    
    result /= float(samples);
    gl_FragColor = vec4(result, 1.0);
} 