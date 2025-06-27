package com.github.vyhovskyi.dao.jdbc;

import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.exception.ServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JdbcGroupDaoTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private JdbcGroupDao dao;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        dao = new JdbcGroupDao(connection);
    }

    @Test
    void getGroupById_shouldReturnGroup() throws Exception {
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        mockGroupResultSet();

        Optional<Group> result = dao.getGroupById(1);

        assertTrue(result.isPresent());
        assertEquals("Test Group", result.get().getName());
    }

    @Test
    void createGroup_shouldReturnGeneratedId() throws Exception {
        when(connection.prepareStatement(any(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(10);

        Group group = getTestGroup();
        Integer id = dao.createGroup(group);

        assertEquals(10, id);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void updateGroup_shouldExecuteWithoutErrors() throws Exception {
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);

        dao.updateGroup(getTestGroup());

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void deleteGroup_shouldExecuteWithoutErrors() throws Exception {
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);

        dao.deleteGroup(1);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void getGroupsByFilter_shouldReturnGroups() throws Exception {
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        mockGroupResultSet();

        List<Group> groups = dao.getGroupsByFilter("Test", "Desc", 10, 0);

        assertEquals(1, groups.size());
        assertEquals("Test Group", groups.get(0).getName());
    }

    private Group getTestGroup() {
        return Group.builder()
                .id(1)
                .name("Test Group")
                .description("Desc")
                .build();
    }

    private void mockGroupResultSet() throws SQLException {
        when(resultSet.getInt("group_id")).thenReturn(1);
        when(resultSet.getString("group_name")).thenReturn("Test Group");
        when(resultSet.getString("group_description")).thenReturn("Desc");
    }
}
