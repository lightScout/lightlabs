precision mediump float;

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform vec2 u_direction;
uniform float u_blurRadius;

varying vec2 v_texCoord;

// Gaussian weights for high quality blur
const float weights[5] = float[](0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

void main() {
    vec2 texelSize = 1.0 / u_resolution;
    vec3 result = texture2D(u_texture, v_texCoord).rgb * weights[0];
    
    for(int i = 1; i < 5; ++i) {
        vec2 offset = float(i) * u_blurRadius * u_direction * texelSize;
        result += texture2D(u_texture, v_texCoord + offset).rgb * weights[i];
        result += texture2D(u_texture, v_texCoord - offset).rgb * weights[i];
    }
    
    gl_FragColor = vec4(result, 1.0);
} 