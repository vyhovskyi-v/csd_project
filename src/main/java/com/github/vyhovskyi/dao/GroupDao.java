package com.github.vyhovskyi.dao;

import com.github.vyhovskyi.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends AutoCloseable {
    List<Group> getAllGroups();
    Optional<Group> getGroupById(Integer id);
    void createGroup(Group group);
    void updateGroup(Group group);
    void deleteGroup(Integer id);
    List<Group> getGroupsByFilter(String groupName, String groupDescription, int limit, int offset);
    List<Group> getGroupsByFilter(String groupName, String groupDescription);

    void close();
}