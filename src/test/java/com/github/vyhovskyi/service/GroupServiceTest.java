package com.github.vyhovskyi.service;

import com.github.vyhovskyi.dao.GroupDao;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GroupServiceTest {

    @Mock
    private GroupDao groupDao;

    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupService = new GroupService(groupDao);
    }

    @Test
    void getAllGroups_ShouldReturnGroups() {
        List<Group> mockGroups = Arrays.asList(
                Group.builder().id(1).name("Food").description("Food group").build(),
                Group.builder().id(2).name("Electronics").description("Electronics group").build()
        );

        when(groupDao.getAllGroups()).thenReturn(mockGroups);

        List<Group> result = groupService.getAllGroups();

        assertEquals(2, result.size());
        verify(groupDao, times(1)).getAllGroups();
    }

    @Test
    void getAllGroups_ShouldThrowServiceException() {
        when(groupDao.getAllGroups()).thenThrow(new RuntimeException("DB error"));

        assertThrows(ServiceException.class, () -> groupService.getAllGroups());
    }

    @Test
    void getGroupById_ShouldReturnGroup() {
        Group group = Group.builder().id(1).name("Household").description("Household items").build();
        when(groupDao.getGroupById(1)).thenReturn(Optional.of(group));

        Optional<Group> result = groupService.getGroupById(1);

        assertTrue(result.isPresent());
        assertEquals("Household", result.get().getName());
    }

    @Test
    void getGroupById_ShouldThrowServiceException() {
        when(groupDao.getGroupById(anyInt())).thenThrow(new RuntimeException("Error"));

        assertThrows(ServiceException.class, () -> groupService.getGroupById(1));
    }

    @Test
    void addGroup_ShouldInvokeDao() {
        Group group = Group.builder().name("NewGroup").description("Description").build();

        groupService.addGroup(group);

        verify(groupDao, times(1)).createGroup(group);
    }

    @Test
    void addGroup_ShouldThrowServiceException() {
        Group group = Group.builder().name("NewGroup").description("Description").build();
        doThrow(new RuntimeException("DB fail")).when(groupDao).createGroup(group);

        assertThrows(ServiceException.class, () -> groupService.addGroup(group));
    }

    @Test
    void getGroupsByFilter_ShouldReturnList() {
        List<Group> mockGroups = List.of(Group.builder().id(1).name("Filtered").description("Filtered desc").build());

        when(groupDao.getGroupsByFilter("Filtered", "Filtered desc", 10, 0)).thenReturn(mockGroups);

        List<Group> result = groupService.getGroupsByFilter("Filtered", "Filtered desc", 10, 0);

        assertEquals(1, result.size());
        assertEquals("Filtered", result.get(0).getName());
    }

    @Test
    void getGroupsByFilter_ShouldThrowServiceException() {
        when(groupDao.getGroupsByFilter(anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("DB fail"));

        assertThrows(ServiceException.class, () -> groupService.getGroupsByFilter("name", "desc", 10, 0));
    }
}
