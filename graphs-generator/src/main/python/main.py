from flask import Flask

from graphsgenerator import graphs_generator_controller

main_route = '/api'
app = Flask(__name__)
app.register_blueprint(graphs_generator_controller.graphsGeneratorRoute, url_prefix=main_route + '/graphs')


@app.route("/")
def hello():
    return "TEST PAGE - SUCCESS"


if __name__ == "__main__":
    app.run(host="localhost", port='5000', debug=True)
