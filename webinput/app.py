from flask import Flask, render_template, request, session, url_for, jsonify

app = Flask(__name__)

@app.route('/')
def show_home():
  return render_template("index.html")

@app.route('/send', methods=['POST'])
def send():
  return jsonify(request.form)

if __name__ == '__main__':
  app.debug = True
  app.run('0.0.0.0', 8080)
