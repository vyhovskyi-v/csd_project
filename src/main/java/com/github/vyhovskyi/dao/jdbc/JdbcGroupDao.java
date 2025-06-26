package com.github.vyhovskyi.dao.jdbc;

import com.github.vyhovskyi.dao.GroupDao;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.exception.ServerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcGroupDao implements GroupDao {

    private static final String GET_ALL = "SELECT * FROM `group`";
    private static final String GET_BY_ID = "SELECT * FROM `group` WHERE group_id = ?";
    private static final String CREATE = "INSERT INTO `group` (group_name, group_description) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE `group` SET group_name=?, group_description=?  WHERE group_id = ?";
    private static final String DELETE = "DELETE FROM `group` WHERE group_id = ?";


    private final Connection connection;

    public JdbcGroupDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        try(Statement query = connection.createStatement(); ResultSet rs = query.executeQuery(GET_ALL)){
            while (rs.next()) {
                groups.add(extractGroupFromResultSet(rs));
            }
        }catch (SQLException e){
            throw new SecurityException(e);
        }
        return groups;
    }

    @Override
    public Optional<Group> getGroupById(Integer id) {
        Optional<Group> group = Optional.empty();
        try(PreparedStatement query = connection.prepareStatement(GET_BY_ID)){
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();
            if(rs.next()) {
                group = Optional.of(extractGroupFromResultSet(rs));
            }
        }catch (SQLException e){
            throw new ServerException(e);
        }
        return group;
    }

    @Override
    public void createGroup(Group group) {
        try(PreparedStatement query = connection.prepareStatement(CREATE)){
            query.setString(1, group.getName());
            query.setString(2, group.getDescription());
            query.executeUpdate();
        }catch (SQLException e){
            throw new ServerException(e);
        }
    }

    @Override
    public void updateGroup(Group group) {
        try(PreparedStatement query = connection.prepareStatement(UPDATE)){
            query.setString(1, group.getName());
            query.setString(2, group.getDescription());
            query.setInt(3, group.getId());
            query.executeUpdate();
        }catch (SQLException e){
            throw new ServerException(e);
        }
    }

    @Override
    public void deleteGroup(Integer id) {
        try(PreparedStatement query = connection.prepareStatement(DELETE)){
            query.setInt(1, id);
            query.executeUpdate();
        }catch (SQLException e){
            throw new ServerException(e);
        }
    }

    @Override
    public List<Group> getGroupsByFilter(String groupName, String groupDescription){
        return getGroupsByFilter(groupName, groupDescription, 0, 0);
    }

    @Override
    public List<Group> getGroupsByFilter(String groupName, String groupDescription, int limit, int offset) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM `group` WHERE 1=1 ");

        List<Object> params = new ArrayList<>();


        if (groupName != null && !groupName.isEmpty()) {
            queryBuilder.append(" AND group_name LIKE ? ");
            params.add("%" + groupName + "%");
        }

        if (groupDescription != null && !groupDescription.isEmpty()) {
            queryBuilder.append(" AND group_description LIKE ? ");
            params.add("%" + groupDescription + "%");
        }

        if (limit > 0) {
            queryBuilder.append(" LIMIT ? ");
            params.add(limit);

            if (offset > 0) {
                queryBuilder.append(" OFFSET ? ");
                params.add(offset);
            }
        }



        List<Group> groups = new ArrayList<>();
        try(PreparedStatement query = connection.prepareStatement(queryBuilder.toString())){
            for( int i = 0; i < params.size(); i++ ) {
                query.setObject(i + 1, params.get(i));
            }
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                groups.add(extractGroupFromResultSet(rs));
            }
        }catch (SQLException e){
            throw new ServerException(e);
        }
        return groups;
    }


    @Override
    public void close(){
        try{
            connection.close();
        }catch (SQLException e){
            throw new ServerException(e);
        }
    }

    protected static Group extractGroupFromResultSet(ResultSet rs) throws SQLException {
        return  Group.builder()
                .id(rs.getInt("group_id"))
                .name(rs.getString("group_name"))
                .description(rs.getString("group_description"))
                .build();
    }


}
