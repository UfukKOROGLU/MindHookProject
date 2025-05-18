from flask import Flask, jsonify, request
from flask_cors import CORS
import pymysql

app = Flask(__name__)
CORS(app)


def get_db_connection():
    return pymysql.connect(
        host='localhost',
        user='root',
        password='',
        database='mindhook_project',
        charset='utf8mb4',
        cursorclass=pymysql.cursors.DictCursor
    )

@app.route('/attendance', methods=['GET'])
def get_attendance():
    try:
        db = get_db_connection()
        with db.cursor() as cursor:
            sql = "SELECT * FROM attendance"
            cursor.execute(sql)
            result = cursor.fetchall()
        db.close()
        return jsonify(result)
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route("/participation/<int:user_id>", methods=["GET"])
def get_participation_score(user_id):
    try:
        db = get_db_connection()
        with db.cursor() as cursor:
            cursor.execute("""
                SELECT 
                    COUNT(*) AS total, 
                    SUM(CASE WHEN status = 'present' THEN 1 ELSE 0 END) AS attended 
                FROM attendance 
                WHERE user_id = %s
            """, (user_id,))
            result = cursor.fetchone()
        db.close()

        if result and result['total'] > 0:
            score = round((result['attended'] / result['total']) * 100, 2)
        else:
            score = 0

        return jsonify({
            "user_id": user_id,
            "score": score
        })
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/api/login', methods=['POST'])
def login_user():
    try:
        data = request.get_json(force=True)
        username = data.get('username')
        password = data.get('password')

        db = get_db_connection()
        with db.cursor() as cursor:
            sql = "SELECT id FROM users WHERE username=%s AND password=%s"
            cursor.execute(sql, (username, password))
            result = cursor.fetchone()
        db.close()

        if result:
            return jsonify({"user_id": result["id"]})
        else:
            return jsonify({"error": "Invalid credentials"}), 401
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/attendance/<int:user_id>', methods=['GET'])
def get_attendance_by_user(user_id):
    try:
        db = get_db_connection()
        with db.cursor() as cursor:
            sql = "SELECT * FROM attendance WHERE user_id = %s"
            cursor.execute(sql, (user_id,))
            result = cursor.fetchall()
        db.close()
        return jsonify(result)
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/profile/<int:user_id>', methods=['GET'])
def get_user_profile(user_id):
    try:
        db = get_db_connection()
        with db.cursor() as cursor:
            sql = "SELECT username FROM users WHERE id = %s"
            cursor.execute(sql, (user_id,))
            result = cursor.fetchone()
        db.close()
        if result:
            return jsonify({"username": result['username']})
        else:
            return jsonify({"error": "User not found"}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/courses', methods=['GET'])
def get_courses():
    try:
        db = get_db_connection()
        with db.cursor() as cursor:
            sql = "SELECT id, name FROM courses"
            cursor.execute(sql)
            result = cursor.fetchall()
        db.close()
        return jsonify(result)
    except Exception as e:
        return jsonify({'error': str(e)})
               

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)