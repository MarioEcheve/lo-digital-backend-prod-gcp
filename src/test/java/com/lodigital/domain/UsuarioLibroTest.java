package com.lodigital.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.lodigital.web.rest.TestUtil;

public class UsuarioLibroTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioLibro.class);
        UsuarioLibro usuarioLibro1 = new UsuarioLibro();
        usuarioLibro1.setId(1L);
        UsuarioLibro usuarioLibro2 = new UsuarioLibro();
        usuarioLibro2.setId(usuarioLibro1.getId());
        assertThat(usuarioLibro1).isEqualTo(usuarioLibro2);
        usuarioLibro2.setId(2L);
        assertThat(usuarioLibro1).isNotEqualTo(usuarioLibro2);
        usuarioLibro1.setId(null);
        assertThat(usuarioLibro1).isNotEqualTo(usuarioLibro2);
    }
}
