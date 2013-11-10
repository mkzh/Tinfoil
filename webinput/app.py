from flask import Flask

app = Flask(__name__)

@app.route('/')
def show_home():
  return render_template("index.html")

if __name__ == '__main__':
  app.run('0.0.0.0', 8080)
