TO DO:


UTILS:
hash_password
generate_jwt_token
verify_password
verify_jwt_token
decode_token_from_header


ADMIN:
jwt_required (route protection)


AUTHENTICATION
/api:
/signup (POST):
return jsonify ({
            "message": "User created successfully",
            "token": token,
            "user": new_user.to_dict()
        }), 201

/login (POST):
return jsonify ({
                "message": "User logged in successfully",
                "token": token,
                "user": existing_user.to_dict()
            }), 200

/logout (POST):
return jsonify({
        "message": "User logged out"
    }), 200

/me (GET):
return jsonify({
        "user": user.to_dict()
    }), 200
