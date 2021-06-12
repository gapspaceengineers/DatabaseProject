package com.project.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.test.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FooTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FooTable.class);
        FooTable fooTable1 = new FooTable();
        fooTable1.setCode(1L);
        FooTable fooTable2 = new FooTable();
        fooTable2.setCode(fooTable1.getCode());
        assertThat(fooTable1).isEqualTo(fooTable2);
        fooTable2.setCode(2L);
        assertThat(fooTable1).isNotEqualTo(fooTable2);
        fooTable1.setCode(null);
        assertThat(fooTable1).isNotEqualTo(fooTable2);
    }
}
